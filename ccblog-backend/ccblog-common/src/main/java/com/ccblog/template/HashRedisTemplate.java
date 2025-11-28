package com.ccblog.template;

import com.ccblog.utils.RedisUtil;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ccblog.enumeration.user.FieldInfoEnum.COLS;


/**
 * redis hash缓存读写模板
 * 模板针对hash存储方式的key
 * 单索引模板
 * 提供未命中回源功能
 * @author czc
 * @date 2025/10/30
 */
@Slf4j
@Component
public abstract class HashRedisTemplate<T> {

    private final long ttl;

    protected HashRedisTemplate() {
        this.ttl = 10*RedisUtil.TTL_MINUTE;
    }

    protected HashRedisTemplate(long ttl) {
        this.ttl = ttl;
    }

    /* key构造规则 */
    protected abstract String buildKey(T idx);
    
    /* 更新redis数据 */
    protected abstract void updateRedis(T idx);

    /* 批量更新redis数据 */
    protected abstract void updateRedisBatch(List<T> idxs);

    /**
     * 获取key某个filed的信息 1+1
     * @param field
     * @param idx
     * @return
     */
    @Nullable // 可能为null
    public String getContent(T idx,String field) {
        String key = buildKey(idx);
        String v = RedisUtil.hGet(key, field);
        if(v==null){ // 这种情况是redis的数据丢失
            updateRedis(idx);
            v = RedisUtil.hGet(key, field);
        }
        return v;
    }

    /**
     * 获取key的fields信息 1+N
     * @param fields
     * @param idx
     * @return
     */
    @Nullable
    public Map<String,String> getContents(T idx,List<String> fields) {
        String key = buildKey(idx);
        Map<String, String> result = RedisUtil.mHGetFields(List.of(key),fields).get(0);
        // 检查miss
        for(String field:fields){
            if(result.get(field)==null){
                updateRedis(idx);
//                log.warn("miss!");
                result = RedisUtil.mHGetFields(List.of(key),fields).get(0);
                break;
            }
        }
        return result;
    }

    /**
     * 查询多个用户的某些域的集合(N+N)
     * 返回值的键为key索引,值为相应的fields查询得到的值
     * @param idxSet        索引集合
     * @param fields         查询域
     * @return
     */
    public Map<T,Map<String, String>> getContentBatch(Set<T> idxSet,List<String> fields){
        List<T> idxList = idxSet.stream().toList();
        if(fields==null){ // 全部
            return Map.of();
        }
        List<String> keys = new ArrayList<>();
        for(T tuple:idxList){
            keys.add(buildKey(tuple));
        }
        List<Map<String, String>> result = RedisUtil.mHGetFields(keys,fields);
        // 统计出未命中的并且批量查询更新
        List<T> missIdxs = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        List<String> missKeys = new ArrayList<>(); // 这里更好的做法是去重之后再查询补充,但是麻烦且本身这种数据不多,没有多大必要
        for(int i=0;i<keys.size();i++){
            Map<String, String> mp = result.get(i);
            for(String field:fields){
                if(mp.get(field)==null){
                    missIdxs.add(idxList.get(i));
                    indexs.add(i); //记录索引位置,方便查到后补进来
                    missKeys.add(keys.get(i));
                    break;
                }
            }
        }
        if(!missIdxs.isEmpty()){ // 有缺失
            // 批量更新(查数据库)
            updateRedisBatch(missIdxs);
            // missIdx重查
            List<Map<String, String>> missInfoList = RedisUtil.mHGetFields(missKeys,fields);
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
     * @param map
     * @param idx
     * @return
     */
    public boolean setContent(T idx,Map<String,String> map) {
        String key = buildKey(idx);
        RedisUtil.hMSet(key, map, ttl);
        return true;
    }

    /**
     * 批量更新用户信息
     */
    public void setContentBatch(List<T> idxList,List<Map<String,String>> fieldsInfo){
        List<String> keys = new ArrayList<>();
        for(T idx:idxList){
            keys.add(buildKey(idx));
        }
        RedisUtil.mHSetBatch(keys,fieldsInfo,ttl);
    }

}