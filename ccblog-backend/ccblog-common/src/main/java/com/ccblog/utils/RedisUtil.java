package com.ccblog.utils;

import com.ccblog.constant.RedisPrefixConstant;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccblog.utils.JsonUtil.fromJson;
import static com.ccblog.utils.JsonUtil.toJsonString;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.function.Consumer;

@Component
public final class RedisUtil {
    private static RedisTemplate<String, String> template;
    private static final String GLOBAL_PREFIX = RedisPrefixConstant.GLOBAL_PREFIX;

    // 常用时间
    public static final Long TTL_WEEK = 604800L; // 一周
    public static final Long TTL_DAY = 86400L; // 一天
    public static final Long TTL_HOUR = 3600L; // 一小时
    public static final Long TTL_MINUTE = 60L; // 一分钟


    public static void setTemplate(RedisTemplate<String, String> redisTemplate) {
        template = redisTemplate;
    }

    public static String buildKey(String rawKey) {
        return GLOBAL_PREFIX + rawKey;
    }

    public static byte[] buildKeyByte(String raw) {
        return buildKey(raw).getBytes();
    }

    public static String getLuaSHA(String script){
        return template.getConnectionFactory()
                .getConnection()
                .scriptLoad(script.getBytes());
    }

    /**
     * 判断某个key是否存在
     * @param key  key值
     * @return
     */
    public static boolean hasKey(String key) {
        return Boolean.TRUE.equals(template.hasKey(buildKey(key)));
    }

    /**
     * 获取pattern匹配的key集合,返回不加前缀的
     */
    public static Set<String> getKeys(String pattern){
        String fullPattern = buildKey(pattern);   // 自动加前缀
        Set<String> result = new HashSet<>();
        // 扫描出所有匹配的key
        template.execute((RedisCallback<Set<String>>) connection -> {
            Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().match(fullPattern).count(1000).build());
            while (cursor.hasNext()) {
                String key = new String(cursor.next(), StandardCharsets.UTF_8);
                // 剥掉前缀(方便后续调用查询)
                if (key.startsWith(GLOBAL_PREFIX)) {
                    result.add(key.substring(GLOBAL_PREFIX.length()));
                }
            }
            return result;
        });
        return result;
    }

    /**
     * 删除 一个 redis key
     * @param key
     */
    public static void remove(String key) {
        String fullKey =buildKey(key);
        byte[] keyBytes = fullKey.getBytes(StandardCharsets.UTF_8);
        template.execute((RedisCallback<Long>) con -> con.commands().del(keyBytes));
    }


    /**
     * 根据 pattern 批量删除 Redis 键
     * @param pattern（不带前缀）
     * @return 实际删除的 key 数量
     */
    public static long deleteByPattern(String pattern) {
        String fullPattern = buildKey(pattern);
        List<String> keys = new ArrayList<>();

        // Scan 拿到所有 key（不阻塞）
        template.execute((RedisConnection connection) -> {
            Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().match(fullPattern.getBytes(StandardCharsets.UTF_8)).count(1000).build());
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
            return null;
        });
        // 修改这里：使用类型转换
        List<Object> results = template.executePipelined((RedisConnection connection) -> {
            for (String k : keys) {
                connection.del(k.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });

        // 将List<Object>转换为List<Long>并且计数
        return results.stream()
                .map(obj -> (Long) obj)
                .mapToLong(Long::longValue)
                .sum();
    }

    /**
     * 批量删除 key（自动拼接前缀）
     * @param rawKeys 原始 key 列表（未加前缀）
     * @return 实际删除的条数
     */
    public static long removeBatch(List<String> rawKeys) {
        if (rawKeys == null || rawKeys.isEmpty()) {
            return 0L;
        }
        return pipelined(conn -> {
            for (String k : rawKeys) {
                conn.del(buildKeyByte(k));
            }
        }).stream()
          .mapToLong(obj -> obj == null ? 0L : (Long) obj)
          .sum();
    }

    /**
     * Redis Pipeline 工具（返回结果已自动解码为 String）
     * 用法：
     *   List<Object> results = RedisUtil.pipelined(conn -> {
     *       conn.hGet(buildKey("k1").getBytes(), "field".getBytes());
     *       conn.hIncrBy(buildKey("k2").getBytes(), "cnt".getBytes(), 1);
     *   });
     * 结果顺序与命令顺序一致
     */
    // 特别注意,不要在Redisson的锁里面再用这个,会出现解锁线程不一致的问题
    public static List<Object> pipelined(Consumer<RedisConnection> commandConsumer) {
        return template.execute((RedisConnection connection) -> {
            connection.openPipeline();
            commandConsumer.accept(connection);          // 下发命令（返回都是 null）
            // closePipeline = 一次性发送 + 返回结果
            return connection.closePipeline();           // List<Object> 已解码
        });
    }


    /* ========== 字符串(原子) ========== */
    public static void set(String key, String value) {
        template.opsForValue().set(buildKey(key), value);
    }

    public static void set(String key, String value, Long ttl_s) {
        if(ttl_s==null || ttl_s<0) set(key, value);
        template.opsForValue().set(buildKey(key), value, Duration.ofSeconds(ttl_s));
    }

    public static String get(String key) {
        return template.opsForValue().get(buildKey(key));
    }

    /**
     * 批量读
     */
    /**
     * 批量读取多个 String 值
     * @param keys  原始 key 列表（不带前缀）
     * @return  与 keys 顺序对应的 value 列表（null 表示缺失）
     */
    public static List<String> mGet(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        // 批量加前缀
        List<String> fullKeys = keys.stream()
                .map(RedisUtil::buildKey)
                .collect(Collectors.toList());
        // 一次网络拉全部
        List<String> values = template.opsForValue().multiGet(fullKeys);
        // multiGet 返回 List<String>，null 表示 Key 不存在
        return values == null ? Collections.emptyList() : values;
    }

    /**
     * 原子递增 delta（可正可负）
     */
    public static long incrBy(String key, long delta) {
        String realKey = buildKey(key);
        try {
            return template.opsForValue().increment(realKey, delta);
        } catch (Exception e) {
            template.delete(realKey);
            return template.opsForValue().increment(realKey, delta);
        }
    }

    /**
     * 原子递增 delta 并带过期时间（仅 key 不存在时设置过期）
     */
    public static long incrBy(String key, long delta, Long ttl_s) {
        String realKey = buildKey(key);
        Long newVal = template.opsForValue().increment(realKey, delta);
        if (ttl_s != null && ttl_s > 0 && newVal != null && newVal.equals(delta)) {
            // 第一次写入才设过期，避免每次刷新 TTL
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return newVal;
    }

    /* ========== 通用对象 ========== */
    public static void setObj(String key, Object obj) {
        template.opsForValue().set(buildKey(key), toJsonString(obj));
    }

    public static void setObj(String key, Object obj, Long ttl_s) {
        if(ttl_s==null||ttl_s<0) setObj(key, obj);
        template.opsForValue().set(buildKey(key), toJsonString(obj), Duration.ofSeconds(ttl_s));
    }

    public static <T> T getObj(String key, Class<T> clazz) {
        String json = template.opsForValue().get(buildKey(key));
        return json == null ? null : fromJson(json, clazz);
    }

    /**
     * 批量获取对象，利用 pipeline 一次性拿回所有 value
     *
     * @param keys   业务 key 列表（未经过 buildKey）
     * @param clazz  要反序列化的类型
     * @param <T>    返回元素类型
     * @return 与 keys 顺序一一对应的结果列表，缺失为 null
     */
    public static <T> List<T> multiGetObj(List<String> keys, Class<T> clazz) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        // 计算redis key
        List<String> finalKeys = keys.stream()
                .map(RedisUtil::buildKey)
                .toList();

        // pipeline 批量 GET
        List<Object> rawValues = template.executePipelined((RedisConnection connection) -> {
            StringRedisConnection strConn = (StringRedisConnection) connection;
            for (String k : finalKeys) {
                strConn.get(k);
            }
            return null;        // pipeline 必须返回 null
        });

        // 逐个反序列化
        List<T> result = new ArrayList<>(rawValues.size());
        for (Object rv : rawValues) {
            result.add(rv == null ? null : fromJson((String) rv, clazz));
        }
        return result;
    }

    /**
     * 批量设置对象（键列表 + 值列表）
     *
     * @param keys       业务 key 列表
     * @param values     对象值列表，与 keys 顺序一一对应
     * @param ttlSeconds 全局 TTL（秒）；null 或 <0 表示永不过期
     * @param <T>        值类型
     */
    public static <T> void multiSetObj(List<String> keys, List<T> values, Long ttlSeconds) {
        if (keys == null || values == null || keys.isEmpty()) {
            return;
        }
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("keys 与 values 长度不一致");
        }

        // 计算最终 Redis key
        List<String> finalKeys = keys.stream().map(RedisUtil::buildKey).toList();

        // pipeline 批量写入
        template.executePipelined((RedisConnection connection) -> {
            StringRedisConnection strConn = (StringRedisConnection) connection;
            for (int i = 0; i < finalKeys.size(); i++) {
                String json = toJsonString(values.get(i));
                String redisKey = finalKeys.get(i);
                if (ttlSeconds != null && ttlSeconds > 0) {
                    strConn.setEx(redisKey, ttlSeconds, json);
                } else {
                    strConn.set(redisKey, json);
                }
            }
            return null;
        });
    }

    /* ------------- 重载：默认永久 ------------- */
    public static <T> void multiSetObj(List<String> keys, List<T> values) {
        multiSetObj(keys, values, null);
    }


    /* ========== Hash 字符串 ========== */
    public static void hSet(String key, String field, String value, Long ttl_s) {
        String realKey = buildKey(key);
        template.opsForHash().put(realKey, field, value);
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
    }

    public static String hGet(String key, String field) {
        return (String) template.opsForHash().get(buildKey(key), field);
    }

    public static void hDel(String key, String... fields) {
        template.opsForHash().delete(buildKey(key), (Object[]) fields);
    }

    /* ========== Hash 加法器 ========== */
    public static long hIncrBy(String key, String field, long delta) {
        return template.opsForHash().increment(buildKey(key), field, delta);
    }

    /* ========== Hash 批量读取 ========== */
    public static Map<Object, Object> hGetAll(String key) {
        return template.opsForHash().entries(buildKey(key));
    }

    /**
     * 每次给hash的一个域写值
     * @param fieldValueMap 域
     */
    public static void hMSet(String key, Map<String, String> fieldValueMap, Long ttl_s) {
        String realKey = buildKey(key);
        template.opsForHash().putAll(realKey, fieldValueMap);
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
    }

    /**
     * 批量更新 hash —— 管道一次性写入（避免 N 次网络往返）
     * @param keys      原始 key 列表
     * @param hashMaps  与 keys 一一对应的 hash 内容
     * @param ttl_s 过期时间（秒）
     */
    public static void mHSetBatch(List<String> keys,
                                  List<Map<String, String>> hashMaps,
                                  long ttl_s) {

        template.executePipelined((RedisConnection conn) -> {
            for (int i = 0; i < keys.size(); i++) {
                byte[] key = buildKey(keys.get(i)).getBytes(StandardCharsets.UTF_8);
                Map<byte[], byte[]> hash = new HashMap<>(hashMaps.get(i).size());
                hashMaps.get(i).forEach((k, v) -> {
                    hash.put(k.getBytes(StandardCharsets.UTF_8),
                            v.getBytes(StandardCharsets.UTF_8));
                });
                // 1. 写 hash
                conn.hMSet(key, hash);
                // 2. 统一设置过期
                conn.expire(key, ttl_s);
            }
            return null;
        });
    }

    /**
     * 用pipe优化一次读取全部的hash内容
     */
    public static List<Map<Object, Object>> mHGetAll(List<String> keys) {
        List<Object> rawResults = template.executePipelined((RedisConnection connection) -> {
            for (String k : keys) {
                connection.hGetAll(buildKey(k).getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
        List<Map<Object, Object>> result = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            result.add((Map<Object, Object>) rawResults.get(i));
        }
        return result;
    }

    /**
     * 用 pipeline 一次读取多个 hash 的指定字段（字段名直接传字符串数组）
     * @param keys   原始 key 列表
     * @param fields 要取的 field 名数组
     * @return 顺序与 keys 一一对应, 每个 Map 只含指定 field
     */
    public static List<Map<String, String>> mHGetFields(List<String> keys, List<String> fields) {
        // 批量发送 HMGET
        List<Object> rawResults = template.executePipelined((RedisConnection conn) -> {
            byte[][] fieldBytes = fields.stream()
                    .map(String::getBytes)
                    .toArray(byte[][]::new);
            for (String k : keys) {
                conn.hMGet(buildKey(k).getBytes(StandardCharsets.UTF_8), fieldBytes);
            }
            return null;
        });

        // 结果处理,按字符串拆包
        List<Map<String, String>> ans = new ArrayList<>(keys.size());
        for (Object o : rawResults) {
            // 这里直接当成 List<String> 用
            List<String> values = (List<String>) o;
            Map<String, String> row = new HashMap<>(fields.size());
            for (int i = 0; i < fields.size(); i++) {
                if(values.get(i)!=null){
                    row.put(fields.get(i), values.get(i));
                }
            }
            ans.add(row);
        }
        return ans;
    }



    /* ========== Hash 长度工具 ========== */
    /**
     * 获得hash类型的key下键对数
     */
    public static Long hSize(String key){
        return template.opsForHash().size(buildKey(key));
    }

    /* ========== Set 工具 ========== */
    /**
     * 写单个值
     * @return 是否新增（true=新增，false=已存在）
     */
    public static Boolean add(String key, String value, Long ttl_s) {
        String realKey = buildKey(key);
        Long added = template.opsForSet().add(realKey, value);
        if (ttl_s != null && ttl_s >= 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return added != null && added == 1L;
    }

    /**
     * 批量写
     * @param key   原始键（会自动经过 buildKey）
     * @param values 要写入的元素集合
     * @param ttl_s  过期时间（秒）；null 或 <0 表示不设过期
     * @return 本次成功插入的元素数量
     */
    public static Long addBatch(String key, List<String> values, Long ttl_s) {
        if (values == null || values.isEmpty()) {
            return 0L;
        }
        String realKey = buildKey(key);
        Long added = template.opsForSet().add(realKey, values.toArray(new String[0]));
        if (ttl_s != null && ttl_s >= 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return added == null ? 0L : added;
    }

    /**
     * 读某个key的全部结果
     */
    public static Set<String> members(String key) {
        return template.opsForSet().members(buildKey(key));
    }

    /**
     * 一次性读取多个 key 的集合
     */
    public static Map<String, Set<String>> multiGet(List<String> keys) {
        Map<String, Set<String>> result = new HashMap<>(keys.size());
        for (String k : keys) {
            result.put(k, template.opsForSet().members(buildKey(k)));
        }
        return result;
    }

    /* ========== ZSet 工具 ========== */
    /**
     * 追加一个成员到 ZSet（score = 当前秒级时间戳）
     * @return 是否新增（true=新增，false=已存在且更新分数）
     */
    public static Boolean zAdd(String key, String member,Long score) {
        return zAdd(buildKey(key), member, score, null);
    }

    /**
     * 追加一个成员到 ZSet（指定 score + TTL）
     * @return 是否新增
     */
    public static Boolean zAdd(String key, String member, double score, Long ttl_s) {
        String realKey = buildKey(key);
        Boolean added = template.opsForZSet().add(realKey, member, score);
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return added != null && added;
    }

    /**
     * 批量追加成员到 ZSet（指定 score + TTL）
     */
    public static Long zAddBatch(String key, Map<String, String> memberScores, Long ttl_s) {
        String realKey = buildKey(key);
        if (memberScores == null || memberScores.isEmpty()) {
            return zSize(realKey);          // 直接返回，避免空写
        }
        // 转换 Map 为 TypedTuple 集合
        Set<ZSetOperations.TypedTuple<String>> tuples = memberScores.entrySet().stream()
                .map(entry -> {
                    double score = Double.parseDouble(entry.getValue());
                    return ZSetOperations.TypedTuple.of(entry.getKey(), score);
                })
                .collect(Collectors.toSet());
        // 批量添加到 zset
        Long added = template.opsForZSet().add(realKey, tuples);
        // 设置 TTL
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return added;
    }

    /**
     * 按分数范围降序读取成员（分页）
     */
    public static Set<String> zGetRange(String key, long start, long end) {
        return template.opsForZSet().reverseRange(buildKey(key), start, end);
    }

    public static Long zSize(String key){
        return template.opsForZSet().zCard(buildKey(key)); // 返回长度
    }

    /**
     * 只保留前 N 名成员（按分数从高到低排序），其余全部删除
     * @param key  业务 key
     * @param n    保留前 n 名
     * @return 实际被删除的成员数量
     */
    public static Long zKeepTopN(String key, long n) {
        String realKey = buildKey(key);
        // 降序下标：0 是最高分，n 就是第 n+1 名
        return template.opsForZSet().getOperations().execute((RedisConnection conn) ->
                conn.zRemRange(realKey.getBytes(), n, -1)
        );
    }

    /**
     * 删除某个成员
     */
    public static Long zRemove(String key, String member) {
        return template.opsForZSet().remove(buildKey(key), member);
    }

    /* ========== List 工具 ========== */
    /**
     * 从右侧追加一个元素到 List（RPUSH）
     * @return 追加后列表长度
     */
    public static Long rPush(String key, String value) {
        return rPush(buildKey(key), value, null);
    }

    /**
     * 从右侧追加一个元素到 List + TTL
     */
    public static Long rPush(String key, String value, Long ttl_s) {
        String realKey = buildKey(key);
        Long newLen = template.opsForList().rightPush(realKey, value);
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return newLen;
    }

    /**
     * 批量右侧追加 List + 统一 TTL
     * @param key    列表 key
     * @param values 待追加元素
     * @param ttl_s  过期秒数（null 表示不设置）
     * @return 插入后列表总长度
     */
    public static long rPushBatch(String key, List<String> values, Long ttl_s) {
        if (values == null || values.isEmpty()) {
            return template.opsForList().size(buildKey(key));
        }
        String realKey = buildKey(key);
        // 一次命令把整个 List 追加到右边
        Long totalLen = template.opsForList().rightPushAll(realKey, values);
        if (ttl_s != null && ttl_s > 0) {
            template.expire(realKey, Duration.ofSeconds(ttl_s));
        }
        return totalLen == null ? 0L : totalLen;
    }

    /**
     * 范围读取 List（升序，左→右）
     * start=0, end=9 → 前 10 条
     */
    public static List<String> lRange(String key, long start, long end) {
        return template.opsForList().range(buildKey(key), start, end);
    }

    /**
     * 读取整个 List（慎用，小数据量）
     */
    public static List<String> listAll(String key) {
        return lRange(key, 0, -1);
    }

}