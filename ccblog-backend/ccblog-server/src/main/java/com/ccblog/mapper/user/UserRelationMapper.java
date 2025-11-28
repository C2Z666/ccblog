package com.ccblog.mapper.user;

import com.ccblog.dto.user.FollowDTO;
import com.ccblog.entity.user.UserRelation;
import com.ccblog.vo.user.FollowUserInfoVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 关于用户关系
 * @author czc
 * @date 2025-09-30
 */
@Mapper
public interface UserRelationMapper {

    /**
     * 保存一条关注关系
     */
    @Insert("insert into user_relation(user_id, follow_user_id, follow_state) " +
            "values (#{userId}, #{followUserId}, #{followed})" +
            "on duplicate key update follow_state = #{followed}")
    void saveRealtionItem(Long userId, Long followUserId, int followed);

    /**
     * 根据用户id查询粉丝数
     * @param userId
     * @return
     */
    @Select("select count(*) from user_relation where follow_user_id=#{userId} and follow_state=1")
    Integer getFanCount(Long userId);

    /**
     * 根据用于id查询关注数
     * @param userId
     * @return
     */
    @Select("select count(*) from user_relation where user_id=#{userId} and follow_state=1")
    Integer getFollowdCount(Long userId);

    /**
     * 用户是否关注当前用户(userId1->userId2)
     * @param userId1
     * @param userId2
     */
    @Select("SELECT COALESCE(" + // 如果为空也是返回0
            "(select (if(follow_state=1,1,0)) from user_relation where user_id=1 and follow_user_id=1)" + // 这个实际就是一句查询,查询不到返回空,否则返回是否关注
            ",0) as ifFollow;")
    Boolean getIFFollow(Long userId1, Long userId2);

    /**
     * 根据用户id查询关注列表
     * @param userId
     * @return
     */
    Page<FollowUserInfoVO> listFollowUser(Long userId);

    /**
     * 根据id查询粉丝量列表(直接一次返回需要的VO)
     * @param userId
     * @return
     */
    Page<FollowUserInfoVO> listFanUser(Long userId);


    /**
     * 根据id查询用户粉丝id(只返回id,提供给后续查询)
     * @return
     */
    @Select("SELECT user_id,last_follow_time FROM user_relation WHERE follow_user_id=#{userId} AND follow_state=1 " +
            "ORDER BY last_follow_time DESC LIMIT #{limit}")
    List<FollowDTO> listFanUserId(Long userId, Long limit);

    /**
     * 根据id查询用户关注id(只返回id,提供给后续查询)
     * @return
     */
    @Select("SELECT follow_user_id AS userId,last_follow_time FROM user_relation WHERE user_id=#{userId} AND follow_state=1 " +
            "ORDER BY last_follow_time DESC")
    List<FollowDTO> listFollowUserId(Long userId);

    /**
     * 批量插入/更细用户关注关系
     * @param userRelationList
     */
    void upsertRelationBatch(List<UserRelation> userRelationList);

    /**
     * 获取最近关注粉丝(days天内)
     * @param userId
     * @return
     */
    List<Long> listRecentFanIds(Long userId,Integer days);
}