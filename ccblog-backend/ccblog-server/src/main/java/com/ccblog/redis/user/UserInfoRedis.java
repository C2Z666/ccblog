package com.ccblog.redis.user;

import com.ccblog.constant.RedisPrefixConstant;
import com.ccblog.dto.user.CommonInfoDTO;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.user.FieldInfoEnum;
import com.ccblog.mapper.user.UserMapper;
import com.ccblog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ccblog.constant.UserConstant.UNKNOW_USER;
import static com.ccblog.constant.UserConstant.UNKNOW_USER_PHOTO;
import static com.ccblog.enumeration.user.FieldInfoEnum.*;

/**
 * 用户相关的通用缓存
 * 理论上这个可以继承 HashRedisTemplate简化,但是无所谓(实现是差不多的)
 * @author czc
 * @date 2025/10/16
 */
@Component
public class UserInfoRedis {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCountRedis userCountRedis;

    /**
     * 查询某个用户的某个域(1+1)
     * @param filed COL_PHOTO/COL_USER_NAME
     * @return null:不存在
     */
    public String getInfo(Long userId,String filed){
        String key = String.format(RedisPrefixConstant.USER_COMMON_INFO,userId);
        String v = RedisUtil.hGet(key,filed);
        if(v==null){
            switch (filed){
                case COL_PHOTO:
                case COL_USER_NAME:
                    updateCommonInfo(userId);
                    break;
                default:
                    ;

            }
            v=RedisUtil.hGet(key,filed);
        }
        return v;
    }

    /**
     * 查询某个用户的多个域(1+N)
     * @param filed COL_PHOTO/COL_USER_NAME
     * @return null:不存在
     */
    public Map<String,String> getInfos(Long userId,List<String> filed){
        String key = String.format(RedisPrefixConstant.USER_COMMON_INFO,userId);
        Map<Object, Object> infoMap = RedisUtil.hGetAll(key);
        if(infoMap.get(filed.get(0))==null){
            updateCommonInfo(userId);
            infoMap = RedisUtil.hGetAll(key);
        }
        return infoMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> (String) e.getKey(),      // key 转 String
                        e -> (String) e.getValue(),  // value 转 String
                        (v1, v2) -> v1));              // 重复 key 策略（可选）
    }



    /**
     * 查询多个用户的某些域的集合(N+N)
     * 返回值的键为用户id
     * 测试使用第一个filed为null判定是否命中,为null会触发查询数据
     */
    public Map<Long,Map<String, String>> getInfosBatch(Set<Long> userIdSet, List<String> fields){
        List<Long> userIds = userIdSet.stream().toList();
        if(fields==null){ // 全部
            fields = List.of(COLS);
        }
        List<String> keys = new ArrayList<>();
        for(Long userId:userIds){
            keys.add(String.format(RedisPrefixConstant.USER_COMMON_INFO,userId));
        }
        List<Map<String, String>> infoList = RedisUtil.mHGetFields(keys,fields);
        // 统计出未命中的并且批量查询更新
        List<Long> missIds = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        List<String> missKeys = new ArrayList<>();
        for(int i=0;i<userIds.size();i++){
            for(String field:fields){
                if(infoList.get(i).getOrDefault(field,null)==null){
                    missIds.add(userIds.get(i));
                    indexs.add(i);  //记录索引位置,方便查到后补进来
                    missKeys.add(keys.get(i));
                }
            }
        }
        if(missIds.size()>0){ // 有缺失
            // 批量更新(查数据库)
            updateCommonInfoBatch(missIds,fields);
            // missId重查
            List<Map<String, String>> missInfoList = RedisUtil.mHGetFields(missKeys,fields);
            // 回填
            for(int i=0;i<missKeys.size();i++){
                infoList.set(indexs.get(i),missInfoList.get(i));
            }
        }
        // 转为目标类型返回
        return IntStream.range(0, infoList.size())
                .boxed()
                .collect(HashMap::new,
                        (m, i) -> m.put(userIds.stream().toList().get(i), infoList.get(i)),
                        HashMap::putAll);
    }

    /**
     * 设置某个用户的信息
     */
    private void setCommonInfo(Long userId,Map<String,String> infoMap){
        String key = String.format(RedisPrefixConstant.USER_COMMON_INFO,userId);
        RedisUtil.hMSet(key, infoMap, RedisUtil.TTL_DAY);
    }

    /**
     * 批量更新用户信息
     */
    private void setCommonInfoBatch(List<Long> userIds,List<Map<String,String>> infoMaps){
        List<String> keys = new ArrayList<>();
        for(Long userId:userIds){
            keys.add(String.format(RedisPrefixConstant.USER_COMMON_INFO,userId));
        }
        RedisUtil.mHSetBatch(keys,infoMaps,RedisUtil.TTL_DAY);
    }

    /**
     * 更新信息某个用户的信息
     */
    private void updateCommonInfo(Long userId){
        UserInfo userInfo = userMapper.getByUserId(userId);
        Map<String,String> map;
        if(userInfo!=null){
            map = Map.of(
                    COL_PHOTO, String.valueOf(userInfo.getPhoto()),
                    COL_USER_NAME, String.valueOf(userInfo.getUserName())
            );
        }
        else {
            map = Map.of(
                    COL_PHOTO, UNKNOW_USER_PHOTO,
                    COL_USER_NAME, UNKNOW_USER
            );
        }
        setCommonInfo(userId,map);
    }

    /**
     * 批量查询用户信息并更新
     * 有三种情况:更新计数和信息,只更新一种
     */
    private void updateCommonInfoBatch(List<Long> userIds, List<String> fields){
        List<CommonInfoDTO> userInfoList = null;
        Map<Long, Map<String, String>> cntMap = null;
        boolean flag_cnt = false;
        boolean flag_info = false;
        if(fields.contains(COL_FAN)){
            flag_cnt=true;
            // 从redis读计数
            cntMap = userCountRedis.getTotalCountsBatch(new HashSet<>(userIds), List.of(COL_FAN));
        }
        if(fields.contains(COL_PHOTO)||fields.contains(COL_USER_NAME)){
           flag_info=true;
           userInfoList = userMapper.getCommInfoByUserIds(userIds);
        }
        // 组装为List<id>和List<Map<filed,value>>准备批量更新
        List<Map<String,String>> idMaps = new ArrayList<>();
        if(flag_info){ // 更新了用户信息
            userIds = new ArrayList<>(); // 顺序不一定和原来一致,需要按照新的顺序逐个匹配组装
            for(CommonInfoDTO commonInfoDTO:userInfoList){
                userIds.add(commonInfoDTO.getUserId());
                if(!flag_cnt){
                    idMaps.add(Map.of(
                            COL_PHOTO, String.valueOf(commonInfoDTO.getPhoto()),
                            COL_USER_NAME, String.valueOf(commonInfoDTO.getUserName())
                    ));
                } else if(cntMap!=null){
                    idMaps.add(Map.of(
                            COL_PHOTO, String.valueOf(commonInfoDTO.getPhoto()),
                            COL_USER_NAME, String.valueOf(commonInfoDTO.getUserName()),
                            COL_FAN, String.valueOf(cntMap.get(commonInfoDTO.getUserId())
                                            .getOrDefault(COL_FAN,"0"))
                            ));
                }
            }
        } else if (cntMap!=null) { // 只更新了用户计数
            for(Long userId:userIds){
                idMaps.add(Map.of(
                        COL_FAN, String.valueOf(cntMap.get(userId).getOrDefault(COL_FAN,"0"))
                ));
            }
        }
        // 批量更新
        setCommonInfoBatch(userIds,idMaps);
    }

    /**
     * 失效某个用户缓存
     */
    public void removeInfo(Long userId){
        String key = String.format(RedisPrefixConstant.USER_COMMON_INFO,userId);
        RedisUtil.remove(key);
    }
}