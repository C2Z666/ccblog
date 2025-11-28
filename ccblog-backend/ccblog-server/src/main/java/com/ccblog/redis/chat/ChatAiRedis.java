package com.ccblog.redis.chat;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.chat.ai.ChatAiAggDTO;
import com.ccblog.event.ChatAiContentUpdEvent;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.JsonUtil;
import com.ccblog.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AI聊天记录异步落库
 * 这个部分还负责一个用户最后时间更新
 * 之所以在用户聊天部分把会话更新并入用户聊天是因为用户聊天部分的会话非常复杂,但是AI聊天部分的很简答
 * @author czc
 * @date 2025/10/28
 */
@Component
@Slf4j
public class ChatAiRedis {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String INTERACTION_INCREASE = "chat:user:content:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "chat:user:content:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private boolean infoFlag;
    private Integer aggregateMaxCount; // 聚合的最大数量(userId数量，同一id多个操作算一个)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public ChatAiRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("chat");
        infoFlag = cfg.isRedisLog();
        aggregateMaxCount = cfg.getMaxCount();
    }

    /**
     * 初始化计数器
     */
    @PostConstruct
    public void init(){
        String version = RedisUtil.get(INCREATE_VERSION);
        if(version==null){
            log.info("【redis:AI聊天】未发现interaction version,初始化");
            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
        }
    }

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(ChatAiAggDTO event) {
        // 版本号 + 队列 key
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);

        // 序列化事件（JSON 字符串）
        String item = JsonUtil.toJsonString(event);

        // 追加到 Redis List 并返回当前长度
        Long cnt = RedisUtil.rPush(key, item, 10*RedisUtil.TTL_MINUTE);

        // 达到阈值立即触发异步落库
        if (cnt >= aggregateMaxCount) {
            if(infoFlag) log.info("【redis:AI聊天】到达阈值 {},触发更新", cnt);
            chatAiUpdatePublish();
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void chatAiUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 存进去的时候序列化了,拿出来需要反序列化为对象,结果为List<NoticeContentEvent>
        List<ChatAiAggDTO> chatAiAggDTOS = RedisUtil.listAll(key).stream()
                .map(s -> JsonUtil.fromJson(s, ChatAiAggDTO.class))
                .collect(Collectors.toList());
        if (chatAiAggDTOS.isEmpty())
        {
            if(infoFlag) log.info("【Redis:AI聊天】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        ChatAiContentUpdEvent chatAiContentUpdEvent = ChatAiContentUpdEvent.builder().
                version(version)
                .chatAiAggDTOS(chatAiAggDTOS).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.CHAT_EVENT_EX, RabbitMQConstant.CHAT_AI_KEY, chatAiContentUpdEvent);
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
        if(infoFlag) log.info("清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    //   ------------------- 定时任务
    @Component("ChatAiContentVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 44 1 * * ? ";
        }
    }

    @Component("ChatAiContentUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            chatAiUpdatePublish();
        }

        @Override
        public String getCron() {
            return "0/10 * * * * ? ";
        }
    }
}