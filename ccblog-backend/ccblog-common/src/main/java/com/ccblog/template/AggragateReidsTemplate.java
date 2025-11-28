package com.ccblog.template;

import com.ccblog.utils.RedisUtil;
import io.vavr.Tuple;
import io.vavr.Tuple2;

/**
 * redis聚合模板
 * 这里主要维护和聚合相关的
 * 本来想给用了异步落库简化的但是发现基本都继承了另一个类,所以很多没有改
 * 只改了ChatUserRedis,作为一个使用的示范,实测可以减少代码大约30左右(主要还是简化了整体的逻辑,提供一个模板)
 * 这个模板里面的getEventListKey()返回两个值写的不好,但是因为在聚合事件里面前面要key后面要version,查询两次感觉性价比不高所以都返回了
 *
 * @author czc
 * @date 2025/11/3
 */
public abstract class AggragateReidsTemplate<T> {  // T->表示聚合事件类
    private final String AGGREGATE_COUNT = "%s:increase:aggregate_count";    // 当前聚合池的数量
    private  final String INCREASE_LIST_PATTERN = "%s:increase:%s";  // 增量事件清单,%s为version,里面的值就是更新清单
    private  final String INCREATE_VERSION_PATTERN = "%s:increase:version"; // 增量事件的版本,自增,每日根据阈值检查
    private  final long DEFAULT_MAX_VERSION = 999999; // 大于这个版本号就把版本号清空

    private String countKey;
    private final String increaseListKey; // 存储更新清单
    private String versionKey;            // version存储的 redis key
    private int aggragateMaxCnt;          // 最大聚合数量
    private long resetVersion;            // version重置阈值

    /**
     * 发布更新活动
     */
    protected abstract void updateEventPublish();

    /**
     * 聚合事件
     * @param event  聚合事件(多个参数封装为类)
     */
    protected abstract void aggregate(T event);


    /**
     * 获得事件清单key和版本号version
     * 这个写法不好,最好改掉(...)
     * @return <version,key>
     */
    public Tuple2<String,String> getEventListKey(){
        String version = RedisUtil.get(versionKey);
        return Tuple.of(version,String.format(increaseListKey, version));
    }

    /**
     * 发布更新事件
     * @return
     */
    protected boolean checkAndPublish(){
        long cnt = RedisUtil.incrBy(countKey,1);
        if(cnt >= aggragateMaxCnt){
            updateEventPublish(); // 发布事件
            // 版本自增
            RedisUtil.incrBy(versionKey,1); // 版本号 +1
        }
        return false;
    }

    /**
     * 检查并重置version
     */
    protected void checkVersion() {
        String version = RedisUtil.get(versionKey);
        if(Long.parseLong(version)>resetVersion){
            RedisUtil.set(versionKey,"0"); // 清空
        }
    }

    /**
     * 清除version对应清单
     * @param version
     * @return          删除条数
     */
    public long clearIncreaseList(String version) {
        String pattern = String.format(versionKey,version); // 清除某个版本的所有key
        return RedisUtil.deleteByPattern(pattern); // 删除缓存
    }

    // 构造方法
    protected AggragateReidsTemplate(String prefix, int maxAggragateCount, long resetVersion) {
        this.increaseListKey = String.format(INCREASE_LIST_PATTERN,prefix,"%s");
        this.versionKey = String.format(INCREATE_VERSION_PATTERN,prefix);
        this.countKey = String.format(AGGREGATE_COUNT,prefix);
        this.aggragateMaxCnt = maxAggragateCount;
        this.resetVersion = resetVersion;
    }

    protected AggragateReidsTemplate(String prefix, int aggragateMaxCnt) {
        this.increaseListKey = String.format(INCREASE_LIST_PATTERN,prefix,"%s");
        this.versionKey = String.format(INCREATE_VERSION_PATTERN,prefix);
        this.countKey = String.format(AGGREGATE_COUNT,prefix);
        this.aggragateMaxCnt = aggragateMaxCnt;
        this.resetVersion=DEFAULT_MAX_VERSION;
    }

    protected void setAggragateMaxCnt(int aggragateMaxCnt){
        this.aggragateMaxCnt=aggragateMaxCnt;
    }




}