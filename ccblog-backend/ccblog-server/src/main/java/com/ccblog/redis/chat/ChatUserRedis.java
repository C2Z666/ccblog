package com.ccblog.redis.chat;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.event.ChatUserContentUpdEvent;
import com.ccblog.template.AggragateReidsTemplate;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.RedisUtil;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.ccblog.utils.JsonUtil.fromJson;
import static com.ccblog.utils.JsonUtil.toJsonString;

/**
 * 用户聊天信息异步落库
 * @author czc
 * @date 2025/10/24
 */
@Component
@Slf4j
public class ChatUserRedis extends AggragateReidsTemplate<ChatUserItemDTO> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final boolean infoFlag;

    /**
     * 读取配置参数
     * @param props 配置
     */
    public ChatUserRedis(AggregateProps props){
        super("chat:user:content", 1);
        AggregateProps.Item cfg = props.of("chat");
        super.setAggragateMaxCnt(cfg.getMaxCount()); // 聚合的最大数量(noticeId数量，同一id多个操作算一个)
        infoFlag = cfg.isRedisLog();
    }

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(ChatUserItemDTO event) {
        // 获得存储key
        String key = super.getEventListKey()._2;

        // 序列化事件（JSON 字符串）
        String item = toJsonString(event);

        // 追加到 Redis List 并返回当前长度
        RedisUtil.rPush(key, item, 2*RedisUtil.TTL_MINUTE);

        // 检查事件发布
        checkAndPublish();
    }

    @Override
    protected void updateEventPublish() {
        Tuple2<String,String> versionAndKey = super.getEventListKey();
        String version = versionAndKey._1;
        String key = versionAndKey._2;
        // 存进去的时候序列化了,拿出来需要反序列化为对象,结果为List<NoticeContentEvent>
        List<ChatUserItemDTO> chatUserContentUpdEventList = RedisUtil.listAll(key).stream()
                .map(s -> fromJson(s, ChatUserItemDTO.class))
                .collect(Collectors.toList());
        if (chatUserContentUpdEventList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:用户聊天内容】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        ChatUserContentUpdEvent chatUserContentUpdEvent = ChatUserContentUpdEvent.builder()
                 .version(version)
                .chatUserItemDTOList(chatUserContentUpdEventList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.CHAT_EVENT_EX, RabbitMQConstant.CHAT_USER_KEY, chatUserContentUpdEvent);
    }

    /**
     * 每天检查版本号,版本计数大于阈值清空版本号
     */
    public void checkVersion() {
        super.checkVersion();
    }

//    ---------------- 定时任务
    @Component("ChatUserContentVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 46 2 * * ? ";
        }
    }

    @Component("ChatUserContentUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            updateEventPublish();
        }

        @Override
        public String getCron() {
            return "0/3 * * * * ? ";
        }
    }
}