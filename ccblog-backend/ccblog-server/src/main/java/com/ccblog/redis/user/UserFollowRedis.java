package com.ccblog.redis.user;

import com.ccblog.cfg.AggregateProps;
import com.ccblog.constant.RabbitMQConstant;
import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.dto.user.FollowDTO;
import com.ccblog.event.UserFollowUpdEvent;
import com.ccblog.mapper.user.UserRelationMapper;
import com.ccblog.template.TimedTaskTemplate;
import com.ccblog.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户关注模块缓存
 * 关注/取关模块 主要包括用户关注列表获得,用户粉丝列表维护,用户关注关系落库
 * @author czc
 * @date 2025/10/16
 */
@Component
@Slf4j
public class UserFollowRedis {
    @Autowired
    private UserRelationMapper userRelationMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${aggregate.max-version:999999}")   // 默认 999999
    private long maxVersion;

    private final String INTERACTION_INCREASE = "user:relation:increase:%s";// 增量事件清单,%s为version,里面的值就是更新清单
    private final String INCREATE_VERSION = "user:relation:increase:version"; // 增量事件的版本,自增,每日根据阈值检查
    private final long MAX_FAN_CNT = 2000; // 缓存最大存储粉丝数,每日把超出的截断
    private final String FAN_INCREASE_IDS = "user:relation:fan_incre_ids"; // 截断检查清单,粉丝增加需要添加

    private final boolean infoFlag;
    private final Integer aggregateMaxCount; // 聚合的最大数量(有一个操作就算一个,不去重)

    /**
     * 读取配置参数
     * @param props 配置
     */
    public UserFollowRedis(AggregateProps props){
        AggregateProps.Item cfg = props.of("user");
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
            log.info("【redis:用户关注】未发现interaction version,初始化");
            RedisUtil.set(INCREATE_VERSION,"0"); // 如果不存在需要初始化
        }
    }


    /**
     * 获取用户的粉丝
     * 调用需要判断粉丝数是否在能够在redis拿到(根据粉丝数判断)
     * return null 表示超出redis存储范围
     */
    public List<Long> getFanList(Long userId,int currentPage,int pageSize){
        int start = (currentPage-1)*pageSize;
        int end = start+pageSize;
        if(end>MAX_FAN_CNT) return null; // 直接返回未命中(超出的部分不存储在redis,有可能某天有时候超出一部分,但是当日会截断)
        String key = String.format(RedisPrefixConstant.USER_FAN_TIME,userId);
        Long size = RedisUtil.zSize(key);
        if(size==0){
            // 更新数据(把最多MAX长度的数据装进去)
            updateFanIds(userId,MAX_FAN_CNT);
        }
        List<Long> userIdList = RedisUtil.zGetRange(key,start,end).stream()
                .map(Long::parseLong)       // 转换为Long
                .collect(Collectors.toList());
        return userIdList;
    }

    /**
     * 加入用户粉丝id
     *  @param fanMap Map<fanUserId,关注时间>  Map<userId, timestamp>
     */
    public void addFan(Long userId, Map<String,String> fanMap){
        String key = String.format(RedisPrefixConstant.USER_FAN_TIME,userId);
        RedisUtil.zAddBatch(key,fanMap,RedisUtil.TTL_DAY);
    }

    /**
     * 删除粉丝id(取关)
     * @param userId
     */
    public void delFan(Long userId,Long fanId){
        String key = String.format(RedisPrefixConstant.USER_FAN_TIME,userId);
        RedisUtil.zRemove(key,fanId.toString());
    }

    /**
     * 用户粉丝增加增量事件,聚合一项
     * @param userId 粉丝增加用户id
     */
    public void aggregateFanIncrease(Long userId){
        RedisUtil.add(FAN_INCREASE_IDS,userId.toString(),null);
    }

    // --- 用户关注
    /**
     * 获取用户的全部关注id,传给前端用
     * 默认有粉丝,要是查不到的话需要更新redis(全装进来)
     */
    public List<Long> getAllFollowList(Long userId){
        String key = String.format(RedisPrefixConstant.USER_FOLLOW_TIME,userId);
        Long size = RedisUtil.zSize(key);
        if(size==0){
            // 更新数据(把最多MAX长度的数据装进去)
            updateFollowIds(userId);
        }
        List<Long> userIdList = RedisUtil.zGetRange(key,0,-1).stream()
                .map(Long::parseLong)       // 转换为Long
                .collect(Collectors.toList());
        return userIdList;
    }

    /**
     * 分页 获取用户的关注id
     * 默认都是合法范围,要是查不到的话需要更新redis(全装进来)
     */
    public List<Long> getFollowList(Long userId,int currentPage,int pageSize){
        int start = (currentPage-1)*pageSize;
        int end = start+pageSize;
        String key = String.format(RedisPrefixConstant.USER_FOLLOW_TIME,userId);
        Long size = RedisUtil.zSize(key);
        if(size==0){
            // 更新数据(把最多MAX长度的数据装进去)
            updateFollowIds(userId);
        }
        List<Long> userIdList = RedisUtil.zGetRange(key,start,end).stream()
                .map(Long::parseLong)       // 转换为Long
                .collect(Collectors.toList());
        return userIdList;
    }

    /**
     * 设置用户关注
     * @param followMap  Map<followUserId,关注时间>  Map<userId, timestamp>
     */
    public void addFollow(Long userId,Map<String,String> followMap){
        String key = String.format(RedisPrefixConstant.USER_FOLLOW_TIME,userId);
        RedisUtil.zAddBatch(key,followMap,10*RedisUtil.TTL_MINUTE); // 本地存10min
    }

    /**
     * 删除关注
     */
    public void delFollow(Long userId, Long followUserId) {
        String key = String.format(RedisPrefixConstant.USER_FOLLOW_TIME,userId);
        RedisUtil.zRemove(key,followUserId.toString());
    }

    // --- 异步落库
    /**
     * 聚合器,接收新的数据并聚合到一个暂存的里面
     * 相当于列一个更新清单,清单内的需要落库,记录规则为:selfId:followId:state(0/1):timestamp
     */
    public void aggregate(Long selfId,Long followId,Integer state) {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE, version);
        // 增量池(直接追加)
        // 构造追加 selfId:followId:state(0/1):timestamp
        long timestamp = System.currentTimeMillis() / 1000; // 秒级时间戳
        String item =  selfId + ":" + followId + ":" + state + ":" + timestamp;
        Long cnt = RedisUtil.rPush(key,item,RedisUtil.TTL_DAY); // 直接得到当前长度
        // 达到长度就触发落库
        if (cnt >= aggregateMaxCount) {
            if(infoFlag) log.warn("【redis:用户关注】到达阈值,触发更新");
            followUpdatePublish();
        }
    }

    /**
     * 发布异步落库事件(双阈值触发:时间或长度)
     */
    public void followUpdatePublish() {
        String version = RedisUtil.get(INCREATE_VERSION);
        String key = String.format(INTERACTION_INCREASE,version);
        // 读取全部的keys对应的redis数据,取出来的map的键就是需要的userId,值是该用户的这段时间的新关注/取关集合
        List<String> followItemList = RedisUtil.listAll(key);
        if (followItemList.isEmpty())
        {
            if(infoFlag) log.info("【Redis:用户关注】清单为空不更新,version={}",version);
            return; // 没有数据就可以返回了(待办清单空)
        }
        UserFollowUpdEvent userFollowUpdEvent = UserFollowUpdEvent.builder().
                version(version)
                .followItemList(followItemList).build();
        // 发布异步落库事件(注意是发到交换机)
        rabbitTemplate.convertAndSend(RabbitMQConstant.USER_EVENT_EX, RabbitMQConstant.USER_FOLLOW_KEY, userFollowUpdEvent);
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


    // --- 私有方法
    /**
     * 检查版本号,版本计数大于阈值清空版本号
     */
    private void checkVersion() {
        if(infoFlag) log.info("【用户关注】清空版本号");
        String version = RedisUtil.get(INCREATE_VERSION);
        if(Long.parseLong(version)>maxVersion){
            RedisUtil.set(INCREATE_VERSION,"0"); // 清空
        }
    }

    /**
     * 检查粉丝列表,把超出长度的截断
     */
    private void checkFanSize(){
        if(infoFlag) log.info("【用户关注】准备清除超出长度粉丝");
        Set<String> userIds = RedisUtil.members(FAN_INCREASE_IDS);
        String key;
        for(String userId:userIds){
            key = String.format(RedisPrefixConstant.USER_FAN_TIME,userId);
            RedisUtil.zKeepTopN(key,MAX_FAN_CNT); // 把用户超出max_fan_cnt的部分截断
        }
        RedisUtil.remove(FAN_INCREASE_IDS); // 删掉key
    }

    /**
     * 获取粉丝列表并更新缓存
     * @param limit 条数(一般为MAX_FAN_CNT)
     */
    private void updateFanIds(Long userId,Long limit){
        List<FollowDTO> fanDTOs = userRelationMapper.listFanUserId(userId,limit);
        Map<String,String> map = convertFollowDTOToMap(fanDTOs);
        addFan(userId,map);
    }

    /**
     * 获取关注列表并更新缓存
     */
    private void updateFollowIds(Long userId){
        List<FollowDTO> followDTOS = userRelationMapper.listFollowUserId(userId);
        if(!followDTOS.isEmpty()){
            Map<String,String> map = convertFollowDTOToMap(followDTOS);
            addFollow(userId,map);
        }
    }

    /**
     * List<FollowDTO> -> Map<String,String> 方便后续插入
     */
    private Map<String, String> convertFollowDTOToMap(List<FollowDTO> followList) {
        return followList.stream()
            .collect(Collectors.toMap(
            dto -> dto.getUserId().toString(),   // key: userId转String
            dto -> String.valueOf(               // value: localDateTime转时间戳转String
                    dto.getLastFollowTime().toEpochSecond(ZoneOffset.UTC) // 转为秒级时间戳
                )
            ));
    }

    //   ------------------- 定时任务
    @Component("UserFollowVersion")
    public class checkVersionTask implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkVersion();
        }

        @Override
        public String getCron() {
            return "* 50 1 * * ? ";
        }
    }

    @Component("UserFollowUpdate")
    public class publishEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            followUpdatePublish();
        }

        @Override
        public String getCron() {
            return "0/5 * * * * ? ";
        }
    }

    /**
     * 截断超出长度的粉丝(只缓存部分)
     */
    @Component("UserFollowCutFan")
    public class cutFanEvent implements TimedTaskTemplate {
        @Override
        public void execute() {
            checkFanSize();
        }

        @Override
        public String getCron() {
            return "* 20 1 * * ? ";
        }
    }


}