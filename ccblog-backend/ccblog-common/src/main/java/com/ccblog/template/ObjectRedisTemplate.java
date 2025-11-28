package com.ccblog.template;

import com.ccblog.utils.RedisUtil;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;


/**
 * redis object缓存读写模板
 * 模板针对object存储方式的key
 * 单索引模板
 * 提供未命中回源功能
 * @author czc
 * @date 2025/11/2
 */
@Component
public abstract class ObjectRedisTemplate<T,OBJ_CLASS> {

    private final long ttl;

    protected ObjectRedisTemplate() {
        this.ttl = RedisUtil.TTL_DAY;
    }

    protected ObjectRedisTemplate(long ttl) {
        this.ttl = ttl;
    }

    /* key构造规则 */
    protected abstract String buildKey(T idx);
    
    /* 更新redis数据 */
    protected abstract void updateRedis(T idx);

    /* 批量更新redis数据 */
    protected abstract void updateRedisBatch(List<T> idxs);

    /* 获得对应类 */
    protected abstract Class<OBJ_CLASS> getRedisClass();


    /**
     * 获取某个key的对象值
     * @param idx
     * @return
     */
    @Nullable // 可能为null
    public OBJ_CLASS getContent(T idx) {
        String key = buildKey(idx);
        OBJ_CLASS v = RedisUtil.getObj(key,getRedisClass());
        if(v==null){ // 这种情况是redis的数据丢失
            updateRedis(idx);
            v = RedisUtil.getObj(key, getRedisClass());
        }
        return v;
    }

    /**
     * 批量查询(N)
     * 返回值的键为key索引,值为相应查询得到的值
     * @param idxSet         索引集合
     * @return
     */
    public Map<T,OBJ_CLASS> getContentBatch(Set<T> idxSet){
        List<T> idxList = idxSet.stream().toList();
        List<String> keys = new ArrayList<>();
        for(T tuple:idxList){
            keys.add(buildKey(tuple));
        }
        List<OBJ_CLASS> result = RedisUtil.multiGetObj(keys,getRedisClass());
        // 统计出未命中的并且批量查询更新
        List<T> missIdxs = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        List<String> missKeys = new ArrayList<>(); // 这里更好的做法是去重之后再查询补充,但是麻烦且本身这种数据不多,没有多大必要
        for(int i=0;i<keys.size();i++){
            OBJ_CLASS mp = result.get(i);
            if(mp==null){
                missIdxs.add(idxList.get(i));
                indexs.add(i); //记录索引位置,方便查到后补进来
                missKeys.add(keys.get(i));
                break;
            }
        }
        if(!missIdxs.isEmpty()){ // 有缺失
            // 批量更新(查数据库)
            updateRedisBatch(missIdxs);
            // missIdx重查
            List<OBJ_CLASS> missInfoList = RedisUtil.multiGetObj(keys,getRedisClass());
            // 回填
            for(int i=0;i<indexs.size();i++){
                result.set(indexs.get(i),missInfoList.get(i));
            }
        }
        // 转为目标类型返回
        return IntStream.range(0, result.size())
                .boxed()
                .collect(HashMap::new,
                        (m, i) ->
                                m.put(idxList.stream().toList().get(i), result.get(i)),
                        HashMap::putAll);
    }

    /**
     * 设置某个key的值
     * @param obj 存入内容
     * @param idx
     * @return
     */
    public boolean setContent(T idx,OBJ_CLASS obj) {
        String key = buildKey(idx);
        RedisUtil.setObj(key, obj, ttl);
        return true;
    }

    /**
     * 批量设置key的值
     */
    public void setContentBatch(List<T> idxList,List<OBJ_CLASS> objList){
        List<String> keys = new ArrayList<>();
        for(T idx:idxList){
            keys.add(buildKey(idx));
        }
        RedisUtil.multiSetObj(keys,objList,ttl);
    }
}