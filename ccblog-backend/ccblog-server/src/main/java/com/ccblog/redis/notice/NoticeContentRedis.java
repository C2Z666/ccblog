package com.ccblog.redis.notice;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.dto.notice.NoticeContentAggDTO;
import com.ccblog.event.NoticeContentUpdEvent;
import com.ccblog.mapper.user.UserCountMapper;
import com.ccblog.template.TimedTaskTemplate;
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

import static com.ccblog.utils.JsonUtil.fromJson;
import static com.ccblog.utils.JsonUtil.toJsonString;

/**
 * 通知内容异步落库缓存模块
 * @author czc
 * @date 2025/10/15
 */
@Component
@Slf4j
public class NoticeContentRedis {
    @Autowired
    private UserCountMapper userCountMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String INTERACTION_INCREASE = "notice:content:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "notice:content:increase:version"; // 增量事件的版本,自增,每日根据阈值检查

    private boolean infoFlag;
    private Integer aggregateMaxCount; // 聚合的最大数量(userId数量，同一id多个操作算一个)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public NoticeContentRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("notice");
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
            if(infoFlag)  log.info("【redis:通知内容】未发现interaction version,初始化");
            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
        }
    }

    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库
     */
    public void aggregate(NoticeContentAggDTO event) {
        // 版本号 + 队列 key
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);

        // 序列化事件（JSON 字符串）
        String item = toJsonString(event);

        // 追加到 Redis List 并返回当前长度
        Long cnt = RedisUtil.rPush(key, item, 2*RedisUtil.TTL_MINUTE);

        // 达到阈值立即触发异步落库
        if (cnt >= aggregateMaxCount) {
            if(infoFlag)  log.info("【redis:通知内容】到达阈值 {},触发更新", cnt);
            noticeUpdatePublish();
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void noticeUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 存进去的时候序列化了,拿出来需要反序列化为对象,结果为List<NoticeContentEvent>
        List<NoticeContentAggDTO> noticeContentUpdEventList = RedisUtil.listAll(key).stream()
                            .map(s -> fromJson(s, NoticeContentAggDTO.class))
                            .collect(Collectors.toList());
        if (noticeContentUpdEventList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:通知内容】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        NoticeContentUpdEvent noticeContentUpdEvent =NoticeContentUpdEvent.builder().
                version(version)
                .noticeContentUpdEventList(noticeContentUpdEventList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.NOTICE_EVENT_EX, RabbitMQConstant.NOTICE_CONTENT_KEY, noticeContentUpdEvent);
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
        if(infoFlag) log.info("【通知内容】清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    //   ------------------- 定时任务
    @Component("NoticeContentVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 42 1 * * ? ";
        }
    }

    @Component("NoticeContentUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            noticeUpdatePublish();
        }

        @Override
        public String getCron() {
            return "0/10 * * * * ? ";
        }
    }

}