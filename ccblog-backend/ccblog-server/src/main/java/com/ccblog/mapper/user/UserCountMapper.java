package com.ccblog.mapper.user;

import com.ccblog.entity.user.UserCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户计数相关查询
 * @author czc
 * @date 2025/10/16
 */
@Mapper
public interface UserCountMapper {

    /**
     * 获得用户计数信息
     * @param userId
     * @return
     */
    @Select("SELECT read_cnt AS readCnt,like_cnt AS likeCnt,collect_cnt AS collectCnt," +
            "comment_cnt AS commentCnt, fan_cnt AS fanCnt, follow_cnt AS follow_cnt" +
            " FROM user_count WHERE user_id=#{userId}")
    UserCount getCountByUserId(Long userId);

    /**
     * 批量插入计数信息(增量信息)
     * @param userCountList
     */
    void upsertDeltaCountBatch(List<UserCount> userCountList);

    /**
     * 批量查询
     * @param userIds
     * @return
     */
    List<UserCount> getArticleCountBatch(List<Long> userIds);

    /**
     * 根据粉丝数量获取用户
     * @param limit 数量
     * @return
     */
    @Select("SELECT user_id FROM user_count ORDER BY fan_cnt DESC,like_cnt DESC LIMIT #{limit}")
    List<Long> getUserOrderByFan(Integer limit);
}