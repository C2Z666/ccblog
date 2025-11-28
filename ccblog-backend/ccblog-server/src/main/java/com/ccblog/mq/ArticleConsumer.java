package com.ccblog.mq;

import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.article.ArticleInteractUpdDTO;
import com.ccblog.enumeration.article.ArticleOperateFieldEnum;
import com.ccblog.enumeration.article.ArticleOperateTypeEnum;
import com.ccblog.event.ArticleConsumerEvent;
import com.ccblog.event.ArticleInteractUpdEvent;
import com.ccblog.entity.article.ArticleCount;
import com.ccblog.event.CountUpdateEvent;
import com.ccblog.event.OperationEvent;
import com.ccblog.mapper.article.ArticleCountMapper;
import com.ccblog.mapper.article.ArticleInteractionMapper;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.utils.JsonUtil;
import com.ccblog.utils.NumUtil;
import com.ccblog.utils.RedisUtil;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;

/**
 * 处理文章相关的mq请求
 * @author czc
 * @date 2025/10/8
 */
@Component
@Slf4j
public class ArticleConsumer {
    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;

    @Autowired
    private ArticleCountMapper articleCountMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ArticleInteractionRedis articleInteractionRedis;

    @Autowired
    private ArticleCountRedis articleCountRedis;

    @Value("${aggregate.article.mq-log:false}")   // 冒号后面是默认值
    private boolean infoFlag;

    @RabbitListener(queues = RabbitMQConstant.ARTICLE_COUNT_Q,ackMode = "MANUAL")
    @Transactional
    public void updateArticleCount(CountUpdateEvent updateEvent, Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag){

//        DebugUtil.textWriterAdd("D:\\java\\projects\\cc_blog\\doc\\压测结果\\importantlog.txt",
//                String.format("mq接收事件:%s",updateEvent.getVersion()));

        if(infoFlag) log.info("【RabbitMQ:文章计数】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        List<String> evevnList = updateEvent.getEvevnList();

        // 事件回放得到更新增量(前面)
        // 注意这里计算出来存的都是增量!!!
        List<ArticleCount> articleCountList = new ArrayList<>();
        Map<Long,Integer> indexMap = new HashMap<>();
        for(String eventJson:evevnList){
            OperationEvent event = JsonUtil.fromJson(eventJson,OperationEvent.class);
            Long articleId = event.getTargetId();
            int type = event.getOperationCode();
            int index = indexMap.getOrDefault(articleId,-1);
            if(index==-1){
                articleCountList.add(ArticleCount.builder().articleId(articleId).build());
                index = articleCountList.size()-1;
                indexMap.put(articleId,index);
            }
            ArticleCount articleCount = articleCountList.get(index);
            if(type== ArticleOperateTypeEnum.READ.getCode()){
                articleCount.setReadCnt(NumUtil.null2Zero(articleCount.getReadCnt())+1);
            }
            else if(type== ArticleOperateTypeEnum.LIKE.getCode()){
                articleCount.setLikeCnt(NumUtil.null2Zero(articleCount.getLikeCnt())+1);
            }
            else if(type== ArticleOperateTypeEnum.COMMENT.getCode()){
                articleCount.setCommentCnt(NumUtil.null2Zero(articleCount.getCommentCnt())+1);
            }
            else if(type== ArticleOperateTypeEnum.CANCEL_LIKE.getCode()){
                articleCount.setLikeCnt(NumUtil.null2Zero(articleCount.getLikeCnt())-1);
            }
            else if(type== ArticleOperateTypeEnum.COLLECT.getCode()){
                articleCount.setCollectCnt(NumUtil.null2Zero(articleCount.getCollectCnt())+1);
            }
            else if(type== ArticleOperateTypeEnum.CANCEL_COLLECT.getCode()){
                articleCount.setCollectCnt(NumUtil.null2Zero(articleCount.getCollectCnt())-1);
            }
            else if(type== ArticleOperateTypeEnum.NEW.getCode()){
               ; // 不管
            }
        }

        try {
            articleCountMapper.upsertDeltaCountBatch(articleCountList);
            if(infoFlag) log.info("【RabbitMQ:文章计数】成功更新{}条数据, tag={}",articleCountList.size(), tag);
            channel.basicAck(tag, false); // 注意Ack和Nack的区别,写成NAck会手动拒绝,进死信队列
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            ArticleConsumerEvent event = new ArticleConsumerEvent(method,updateEvent.getVersion(),updateEvent.getShardId()); // this为当前类
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列
                log.warn("【文章计数】推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    @RabbitListener(queues = RabbitMQConstant.ARTICLE_INTERACTION_Q,ackMode = "MANUAL")
    @Transactional
    public void updateArticleInteraction(ArticleInteractUpdEvent articleInteractUpdEvent, Channel channel,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        if(infoFlag) log.info("【RabbitMQ:文章交互】rabbitmq更新数据库, tag={}", tag); // tag是一个类似计数器的东西
        Map<String, Set<String>> mp = articleInteractUpdEvent.getIdMap(); // 得到 Map<userId,Set<articleId>>

        // 到redis拿数据更新
        List<String> lastReadKeys = new ArrayList<>();    // 查lastReadTime需要的key集合
        List<String> interactionKeys = new ArrayList<>(); // 查interaction需要的key集合
        List<Pair<String,String>> indexList = new ArrayList<>(); // 预分配位置,方便后续组装
        for(String k: mp.keySet()){
            String userId = k.substring(k.lastIndexOf(':') + 1);
            // 处理用户需要的信息
            for(Object t:mp.get(k)){ // 这里的key实际对应就是articleId
                String articleId = t.toString();
                lastReadKeys.add(String.format(ARTICLE_LAST_READ_TIME,userId,articleId));
                interactionKeys.add(String.format(ARTICLE_INTERACTION,userId,articleId));
                indexList.add(Pair.of(userId,articleId));
            }
        }
        // 获得所有相应数据
        List<String> lastReadList = RedisUtil.mGet(lastReadKeys);
        List<Map<Object, Object>> interactionMap = RedisUtil.mHGetAll(interactionKeys);
        // 组装为需要的数据
        List<ArticleInteractUpdDTO> articleInteractUpdDTOList = new ArrayList<>();
        for(int i=0;i<indexList.size();i++){
            // 创建基本的更新单元
            Long userId = Long.parseLong(indexList.get(i).getFirst());
            Long articleId = Long.parseLong(indexList.get(i).getSecond());
            ArticleInteractUpdDTO articleInteractUpdDTO = ArticleInteractUpdDTO.builder()
                    .userId(userId)
                    .articleId(articleId).build();
            // 加入阅读时间信息
            if(lastReadList.get(i)!=null){
                Long t = Long.parseLong(lastReadList.get(i)); // 最后阅读时间顺序和索引顺序是一样的
                LocalDateTime ldt = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(t), // 这一这里是毫秒时间戳转换
                        ZoneOffset.UTC); // 转为LocalDateTime
                articleInteractUpdDTO.setLastReadTime(ldt);
            }
            // 加入点赞状态等信息
            Map<Object, Object> status = interactionMap.get(i); // 上面已经统一拿到,但是因为顺序被打乱所以
            if(status!=null){
                for(Object filed:status.keySet()){
                    String value = status.get(filed).toString();
                    switch (filed.toString()){
                        case ArticleOperateFieldEnum.COL_READ:
                            articleInteractUpdDTO.setReadStat(Integer.parseInt(value)); // 取出索引位置的对象并赋值
                            break;
                        case ArticleOperateFieldEnum.COL_LIKE:
                            articleInteractUpdDTO.setLikeStat(Integer.parseInt(value));
                            break;
                        case ArticleOperateFieldEnum.COL_COLLECT:
                            articleInteractUpdDTO.setCollectStat(Integer.parseInt(value));
                            break;
                        case ArticleOperateFieldEnum.COL_COMMENT:
                            articleInteractUpdDTO.setCommentStat(Integer.parseInt(value));
                            break;
                        default:
                            // 后续可能扩展其他(比如打分、举报等,一般低频的直接写数据库可能更好)
                            ;
                    }
                }
            }
            articleInteractUpdDTOList.add(articleInteractUpdDTO);
        }

        try {
            articleInteractionMapper.upsertInteractionBatch(articleInteractUpdDTOList);
            if(infoFlag) log.info("【RabbitMQ:文章交互】成功更新{}条数据, tag={}",articleInteractUpdDTOList.size(), tag);
            channel.basicAck(tag, false); // 注意Ack和Nack的区别,写成NAck会手动拒绝,进死信队列
            String method = Thread.currentThread().getStackTrace()[1].getMethodName();
            ArticleConsumerEvent event = new ArticleConsumerEvent(method,
                    articleInteractUpdEvent.getVersion(),articleInteractUpdEvent.getShardId()); // this为当前类
            applicationEventPublisher.publishEvent(event); // 发布清除缓存事件
        } catch (Exception e) {
            try {
                channel.basicNack(tag, false, false); // 丢入死信队列
                log.warn("推送至死信队列,tag={}",tag);
            } catch (IOException ioEx) {
                log.error("NACK 失败", ioEx);
            }
        }
    }

    /**
     * 监听清理缓存请求
     * @param event 事件参数
     */
    @EventListener
    protected void handleClearInteraction(ArticleConsumerEvent event){
        if(event.getSource().toString().equals("updateArticleCount")){
            articleCountRedis.clearIncreaseList(event.getShardId(),event.getVersion());
        } else if (event.getSource().toString().equals("updateArticleInteraction")) {
            articleInteractionRedis.clearIncreaseList(event.getShardId(),event.getVersion());
        }
    }
}