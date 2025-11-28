-- 批量插入用户阅读状态并过滤时间窗内多次重复事件
-- KEYS:   [doneKey1, timeKey1, doneKey2, timeKey2, ...]
-- ARGV:   [newMs1, ttlDone1, ttlTime1, newMs2, ttlDone2, ttlTime2, ...]
-- 返回：   0/1 数组
-- 注意点:  lua下标从1开始
local n = #KEYS / 2
local ret = {}
for i = 1, n do
    local doneKey = KEYS[i*2-1]
    local timeKey = KEYS[i*2]
    local newMs   = tonumber(ARGV[i*3-2])
    local ttlDone = tonumber(ARGV[i*3-1])
    local ttlTime = tonumber(ARGV[i*3])

--    redis.call('ECHO', 'newMs='..(newMs or 'nil')..',ttlDone='..(ttlDone or 'nil')..',ttlTime='..(ttlTime or 'nil'))

    local done  = redis.call('GET', doneKey)
    local oldMsRaw = redis.call('GET', timeKey)

    redis.call('SET', timeKey, newMs, 'EX', ttlTime)

    local oldMs = oldMsRaw and tonumber(oldMsRaw) or nil

    redis.call('SET', timeKey, newMs, 'EX', ttlTime)

    if not done and (not oldMs or newMs - oldMs > 3000) then
        redis.call('SET', doneKey, '1', 'EX', ttlDone)
        ret[i] = 1
    else
        ret[i] = 0
    end
end
return ret