package com.ccblog.redis.chat;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.entity.chat.ChatUserSession;
import com.ccblog.event.ChatUserSessionUpdEvent;
import com.ccblog.mapper.chat.ChatUserMapper;
import com.ccblog.mapper.chat.ChatUserSessionMapper;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.ccblog.constant.RedisPrefixConstant.*;
import static com.ccblog.enumeration.chat.UserSessionFieldEnum.*;

/**
 * 用户聊天会话缓存
 * @author czc
 * @date 2025/10/25
 */
@Component
@Slf4j
public class ChatUserSessionRedis {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ChatUserSessionMapper chatUserSessionMapper;

    @Autowired
    private ChatUserMapper chatUserMapper;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String AGGREGATE_COUNT = "chat:user:session:increase:aggregate_count";// 当前聚合池的数量
    private final String INTERACTION_INCREASE = "chat:user:session:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "chat:user:session:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private final boolean infoFlag;
    private final Integer aggregateMaxCount; // 聚合的最大数量(事件数量)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public ChatUserSessionRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("chat");
        aggregateMaxCount = cfg.getMaxCount(); // 聚合的最大数量(noticeId数量，同一id多个操作算一个)
        infoFlag = cfg.isRedisLog();
    }

    /**
     * 初始化计数器
     */
//    @PostConstruct
//    public void init(RedisUtil redisUtil){
//        String version = RedisUtil.get(INCREATE_VERSION);
//        if(version==null){
//            log.info("【redis:用户会话】未发现interaction version,初始化");
//            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
//        }
//    }

    // 读/写相关操作
    /**
     * 设置值
     */
    public void setSession(Long userId,Long peerId, Map<String,String> map){
        String key = String.format(CHAT_USER_SESSION,userId,peerId);
        RedisUtil.hMSet(key, map, RedisUtil.TTL_DAY);
    }

    /**
     * 获取某个用户和另一个用户当前会话（指定字段）
     * @param field
     * @return 当前数值；不存在返回 0
     */
    public Object getSession(Long userId,Long peerId, String field) {
        if(field.equals(COL_SEQ)&&userId>peerId){
            Long tmp=userId;userId=peerId;peerId=tmp; // 交换,保证读取的一定是(min,max)
        }
        String key = String.format(CHAT_USER_SESSION, userId,peerId);
        String v = RedisUtil.hGet(key, field);
        if(v==null){ // 这种情况是redis的数据丢失
            updateUserSession(userId,peerId);
            v = RedisUtil.hGet(key, field);
        }
        return v;
    }

    /**
     * 失效某个用户的某个聊天快照
     */
    public void clearSession(Long userId,Long peerId){
        String key = String.format(CHAT_USER_SESSION,userId,peerId);
        RedisUtil.remove(key);
    }


    /**
     * 用户未读数增/减
     * @param userId 用户ID
     * @param peerId 对方用户ID
     * @param delta     +1 或 -1（或任意长整型）
     * @return 加完后的最新值
     */
    public long incrUnread(Long userId,Long peerId, long delta) {
        String key = String.format(CHAT_USER_SESSION, userId,peerId);
        return RedisUtil.hIncrBy(key, COL_UNREAD_CNT, delta);
    }


    /**
     * seq++,注意是两人共享的
     * @param userId
     * @param peerId
     */
    public long incrSeq(Long userId, Long peerId,long delta) {
        Long minId = Math.min(userId,peerId);
        Long maxId = Math.max(userId,peerId);
        String key = String.format(CHAT_USER_SESSION, minId,maxId);
        if(!RedisUtil.hasKey(key)){
            updateUserSession(minId,maxId);
        }
        return RedisUtil.hIncrBy(key, COL_SEQ, delta);
    }


    /**
     * 失效某个用户的全部已读
     * @param userId
     */
    public void restUnreadByUserId(Long userId) {
        String pattern = String.format(CHAT_USER_SESSION,userId,"*");
        List<String> keys = RedisUtil.getKeys(pattern).stream().toList();
        List<Map<String,String>> mapList = new ArrayList<>();
        for(int i=0;i<keys.size();i++){
            mapList.add(Map.of(COL_UNREAD_CNT,"0"));
        }
        RedisUtil.mHSetBatch(keys,mapList,RedisUtil.TTL_DAY);
    }


    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(Long userId, Long peerId) {
        // 版本号 + 队列 key
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);

        // 增量池
        Boolean success = RedisUtil.add(key,userId.toString()+":"+peerId,RedisUtil.TTL_DAY); // 是否需要主动失效?
        if(success){
            long cnt = RedisUtil.incrBy(AGGREGATE_COUNT,1); // 聚合数量 +1
            // 达到长度就触发落库
            if (cnt >= aggregateMaxCount) {
                if(infoFlag) log.info("【redis:用户会话】到达阈值,触发更新");
                chatUserSessionUpdPublish();
            }
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void chatUserSessionUpdPublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 需要更新的用户id集合
        List<String> idPairList = RedisUtil.members(key).stream().toList();
        if (idPairList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:用户会话】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        RedisUtil.set(AGGREGATE_COUNT,"0"); // 清空聚合计数信息
        ChatUserSessionUpdEvent chatUserContentUpdEvent = ChatUserSessionUpdEvent.builder().
                version(version)
                .idPairList(idPairList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.CHAT_EVENT_EX, RabbitMQConstant.CHAT_USER_SESSION_KEY, chatUserContentUpdEvent);
        // 版本自增
        RedisUtil.incrBy(INCREATE_VERSION,1); // 版本号 +1
    }

    /**
     * 清除version对应清单
     * @param version
     */
    public void clearIncreaseList(String version) {
        String pattern = String.format(INTERACTION_INCREASE,version); // 清除某个版本的所有key
        long cnt = RedisUtil.deleteByPattern(pattern); // 删除缓存
    }

    /**
     * 检查版本号,版本计数大于阈值清空版本号
     */
    public void checkVersion() {
        if(infoFlag) log.info("【用户会话】清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    // 更新两个用户的聊天会话
    private void updateUserSession(Long userId, Long peerId) {
        ChatUserSession chatUserSession = chatUserSessionMapper.getSessionByUserId(userId,peerId);
        Map<String, String> map;
        if(chatUserSession !=null){
            map = Map.of(
                    COL_LAST_TIME, chatUserSession.getLastMsgTime().toString(),
                    COL_SNAPSHOT, chatUserSession.getSnapshot(),
                    COL_UNREAD_CNT, chatUserSession.getUnreadCount().toString(),
                    COL_DISPLAY_SEQ,chatUserSession.getDisplaySeq().toString(),
                    COL_SEQ,chatUserSession.getSeq().toString()
            );
        }
        else{ // 稳健性考虑(查不到会话记录)
            map = Map.of(
                    COL_LAST_TIME, LocalDateTime.now().toString(),
                    COL_SNAPSHOT, "",
                    COL_UNREAD_CNT, "0",
                    COL_DISPLAY_SEQ,"0",
                    COL_SEQ,"0"
            );
        }
        setSession(userId,peerId,map);
    }


    //    -------- 定时任务
    @Component("ChatUserSessVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 28 1 * * ? ";
        }
    }

    @Component("ChatUserSessUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            chatUserSessionUpdPublish();
        }

        @Override
        public String getCron() {
            return "0/3 * * * * ? ";
        }
    }

}