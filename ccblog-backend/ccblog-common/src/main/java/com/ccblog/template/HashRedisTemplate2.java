package com.ccblog.template;

import com.ccblog.utils.RedisUtil;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import io.vavr.Tuple2;
import java.util.*;
import java.util.stream.IntStream;

import static com.ccblog.enumeration.user.FieldInfoEnum.COLS;


/**
 * redis hash缓存读写模板
 * 模板针对hash存储方式的key
 * 两索引模板
 * 提供未命中回源功能
 * @author czc
 * @date 2025/10/30
 */
@Component
public abstract class HashRedisTemplate2<T1,T2> {

    private final long ttl;

    protected HashRedisTemplate2() {
        this.ttl = 10*RedisUtil.TTL_MINUTE;
    }

    protected HashRedisTemplate2(long ttl) {
        this.ttl = ttl;
    }

    /* key构造规则 */
    protected abstract String buildKey(Tuple2<T1,T2> idxPair);
    
    /* 更新redis数据 */
    protected abstract void updateRedis(Tuple2<T1,T2> idxPair);

    /* 批量更新redis数据 */
    protected abstract void updateRedisBatch(List<Tuple2<T1,T2>> idxPairs);

    public long getTtl(){
        return ttl;
    }

    /**
     * 获取key某个filed的信息 1+1
     * @param field
     * @param idxPair
     * @return
     */
    @Nullable // 可能为null
    public String getContent(Tuple2<T1,T2> idxPair,String field) {
        String key = buildKey(idxPair);
        String v = RedisUtil.hGet(key, field);
        if(v==null){ // 这种情况是redis的数据丢失
            updateRedis(idxPair);
            v = RedisUtil.hGet(key, field);
        }
        return v;
    }

    /**
     * 获取key的fields信息 1+N
     * @param fields
     * @param idxPair
     * @return
     */
    @Nullable
    public Map<String,String> getContents(Tuple2<T1,T2> idxPair,List<String> fields) {
        String key = buildKey(idxPair);
        Map<String, String> result = RedisUtil.mHGetFields(List.of(key),fields).get(0);
        // 检查miss
        for(String field:fields){
            if(result.get(field)==null){
                updateRedis(idxPair);
                result = RedisUtil.mHGetFields(List.of(key),fields).get(0);
                break;
            }
        }
        return result;
    }

    /**
     * 查询多个用户的某些域的集合(N+N)
     * 返回值的键为key索引,值为相应的fields查询得到的值
     * @param idxPairSet     索引集合
     * @param fields         查询域
     * @return
     */
    public Map<Tuple2<T1,T2>,Map<String, String>> getContentBatch(Set<Tuple2<T1,T2>> idxPairSet,List<String> fields){
        List<Tuple2<T1,T2>> idxPairList = idxPairSet.stream().toList();
        if(fields==null){
            return Map.of();
        }
        List<String> keys = new ArrayList<>();
        for(Tuple2<T1,T2> tuple:idxPairSet){
            keys.add(buildKey(tuple));
        }
        List<Map<String, String>> result = RedisUtil.mHGetFields(keys,fields);
        // 统计出未命中的并且批量查询更新
        List<Tuple2<T1,T2>> missIdxs = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        List<String> missKeys = new ArrayList<>(); // 这里更好的做法是去重之后再查询补充,但是麻烦且本身这种数据不多,没有多大必要
        for(int i=0;i<keys.size();i++){
            Map<String, String> mp = result.get(i);
            for(String field:fields){
                if(mp.get(field)==null){
                    missIdxs.add(idxPairList.get(i));
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
                                m.put(idxPairList.stream().toList().get(i), result.get(i)),
                        HashMap::putAll);
    }

    /**
     * 设置某个key的值
     * @param map
     * @param idxPair
     * @return
     */
    public boolean setContent(Tuple2<T1,T2> idxPair,Map<String,String> map) {
        String key = buildKey(idxPair);
        RedisUtil.hMSet(key, map, ttl);
        return true;
    }

    /**
     * 批量更新用户信息
     */
    public void setContentBatch(List<Tuple2<T1,T2>> idxPairList,List<Map<String,String>> fieldsInfo){
        List<String> keys = new ArrayList<>();
        for(Tuple2<T1,T2> idxPair:idxPairList){
            keys.add(buildKey(idxPair));
        }
        RedisUtil.mHSetBatch(keys,fieldsInfo,ttl);
    }

}