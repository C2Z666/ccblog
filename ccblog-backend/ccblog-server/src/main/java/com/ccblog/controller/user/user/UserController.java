package com.ccblog.controller.user.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccblog.annotation.Permission;
import com.ccblog.dto.user.BaseUserInfoDTO;
import com.ccblog.dto.user.UserRelationItemDTO;
import com.ccblog.dto.user.UserSaveInfoDTO;
import com.ccblog.entity.user.UserInfo;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.user.UserRelationService;
import com.ccblog.service.user.UserService;
import com.ccblog.vo.user.FollowUserInfoVO;
import com.ccblog.vo.user.SimpleUserInfoVO;
import com.ccblog.vo.user.UserBaseInfoVo;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Digits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户主页相关接口
 * @author czc
 * @date 2025-09-30
 */
@RestController
@Slf4j
@Tag(name="用户信息",description = "用户信息相关接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRelationService userRelationService;


    /**
     * 获取用户主页信息(历史记录,粉丝...)
     * @param userId
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping("/userHome")
    @Operation(summary = "获取用户主页信息")
    public ResultVo<UserBaseInfoVo> getUserHome(@RequestParam(name="userId") Long userId) throws JsonProcessingException {
        UserBaseInfoVo userBaseInfoVo = userService.getUserHomeInfo(userId);
        return ResultVo.ok(userBaseInfoVo);
    }

    /**
     * 保存用户详细信息(注册)
     * @param userSaveInfoDTO
     * @return
     */
    @Permission(role=UserRole.LOGIN)
    @Operation(summary = "保存用户信息")
    @PostMapping("/saveUserInfo")
    public ResVo saveUserInfo(@RequestBody UserSaveInfoDTO userSaveInfoDTO){
        userService.saveUserInfo(userSaveInfoDTO);
        return ResVo.ok(true);
    }

    /**
     * 保存用户关系
     *
     * @param userRelationItemDTO
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "/saveUserRelation")
    @Operation(summary = "保存/更新关注关系")
    public ResVo<Boolean> saveUserRelation(@RequestBody UserRelationItemDTO userRelationItemDTO) {
        userRelationService.saveUserRelation(userRelationItemDTO);
        return ResVo.ok(true);
    }

    /**
     * 获取用户的关注列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Operation(summary = "获取用户关注列表")
    @GetMapping("/follows")
    public ResultVo<IPage<FollowUserInfoVO>> getUserFollowed(@RequestParam(name = "userId") Long userId,
                                                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        IPage<FollowUserInfoVO> followUserInfoDTOIPage = userRelationService.getPageUserFollowList(userId, currentPage, pageSize);
        return ResultVo.ok(followUserInfoDTOIPage);
    }

    /**
     * 分页获取用户的关注列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Operation(summary = "分页获取用户粉丝列表")
    @GetMapping("/fans")
    public ResultVo<IPage<FollowUserInfoVO>> getUserFans(@RequestParam(name = "userId") Long userId,
                                                             @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        IPage<FollowUserInfoVO> fanUserInfoDTOIPage = userRelationService.getPageUserFanList(userId, currentPage, pageSize);
        return ResultVo.ok(fanUserInfoDTOIPage);
    }

    /**
     * 获取用户的关注列表
     * @param userId
     * @return
     */
    @Operation(summary = "获取用户关注用户id")
    @GetMapping("/followIds")
    public ResVo<List<Long>> getUserFans(@RequestParam(name = "userId") Long userId) {
        return ResVo.ok(userRelationService.getFollowIdsByUserId(userId));
    }

    /**
     * 获取热门用户
     * @return
     */
    @Operation(summary = "获取热门用户")
    @GetMapping("/hotUser")
    public ResVo<List<SimpleUserInfoVO>> getHotUsers(
                                        @RequestParam(name = "limit",required = false,defaultValue = "5") Integer limit) {
        return ResVo.ok(userService.getHotUsersByFanCnt(limit));
    }
}