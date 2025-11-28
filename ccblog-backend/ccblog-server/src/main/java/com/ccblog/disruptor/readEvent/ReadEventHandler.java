package com.ccblog.disruptor.readEvent;

import com.ccblog.enumeration.article.ArticleOperateTypeEnum;
import com.ccblog.enumeration.user.UserOperateFieldEnum;
import com.ccblog.enumeration.user.UserOperateTypeEnum;
import com.ccblog.event.OperationEvent;
import com.ccblog.event.ReadEvent;
import com.ccblog.redis.article.ArticleCountRedis;
import com.ccblog.redis.article.ArticleInteractionRedis;
import com.ccblog.redis.user.UserCountRedis;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.RedisUtil;
import com.lmax.disruptor.EventHandler;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ccblog.constant.RedisPrefixConstant.*;

@Component
@Slf4j
public class ReadEventHandler implements EventHandler<ReadEvent>, TimedTaskTemplate {

    @Autowired
    private ArticleInteractionRedis articleInteractionRedis;

    @Autowired
    private ArticleCountRedis articleCountRedis;

    @Autowired
    private UserCountRedis userCountRedis;


    private final List<ReadEvent> buffer = new ArrayList<>(1024);


    @Override
    public void onEvent(ReadEvent event, long sequence, boolean endOfBatch) {
        buffer.add(event);
        if (buffer.size() >= 1024) {
            flush();
        }
    }

    private void flush() {
//        log.info("【ReadEvent】发布事件,长度:{}",buffer.size());
        if (buffer.isEmpty()) return;
        // 先批量获得是否新事件并获得更新文章列表
        List<ReadEvent> readEvents = buffer.stream().toList();
        buffer.clear();
        // 处理文章阅读状态,并根据时间锁判定新事件
        Set<ReadEvent> newEvents = articleInteractionRedis.setLastReadTimeBatch(readEvents);
        // pipeline 批量写 Redis
        RedisUtil.pipelined(conn -> {
            // 更新数据
            for(ReadEvent event:newEvents){
                long userId   = event.getUserId();
                long authorId = event.getAuthorId();
                long articleId = event.getArticleId();
                byte[] interactKey = RedisUtil.buildKeyByte(
                        String.format(ARTICLE_INTERACTION, userId, articleId));

                conn.hSet(interactKey, UserOperateFieldEnum.COL_READ.getBytes(), "1".getBytes());
                conn.expire(interactKey, articleInteractionRedis.getTtl());

                conn.hIncrBy(RedisUtil.buildKeyByte(
                                String.format(ARTICLE_TOTAL_COUNT, articleId)),
                        UserOperateFieldEnum.COL_READ.getBytes(), 1);

                conn.hIncrBy(RedisUtil.buildKeyByte(
                                String.format(USER_TOTAL_COUNT, authorId)),
                        UserOperateFieldEnum.COL_READ.getBytes(), 1);
            }
        });
        // 批量事件聚合
        List<OperationEvent> articleOpEvents = new ArrayList<>();
        List<OperationEvent> userOpEvents = new ArrayList<>();
        Set<Tuple2<Long,Long>> userArticleIds = new HashSet<>();
        for(ReadEvent event:newEvents){
            articleOpEvents.add(new OperationEvent(event.getArticleId(),ArticleOperateTypeEnum.READ.getCode()));
            userOpEvents.add(new OperationEvent(event.getUserId(), UserOperateTypeEnum.READ.getCode()));
            userArticleIds.add(Tuple.of(event.getUserId(), event.getArticleId()));
        }
        // 文章计数
        articleCountRedis.aggregateBatch(articleOpEvents);
        // 用户计数
        userCountRedis.aggregateBatch(userOpEvents);
        // 阅读状态
        articleInteractionRedis.aggregateBatch(userArticleIds.stream().toList());
    }


    /**
     * 定时执行
     */
    @Override
    public void execute() {
        flush();
    }

    @Override
    public String getCron() {
        return "0/5 * * * * ?";
    }


}