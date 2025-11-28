package com.ccblog.controller.user.notice;

import com.ccblog.annotation.Permission;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.mapper.notice.NoticeContentMapper;
import com.ccblog.service.notice.NoticeService;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import com.ccblog.vo.notice.NoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知模块
 * @author czc
 * @date 2025/10/19
 */
@Slf4j
@Tag(name="通知接口",description = "通知相关接口")
@RequestMapping("/notice")
@RestController
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeContentMapper noticeContentMapper;

    /**
     * 获取各类消息
     * @param type 消息类型 @NotifyTypeEnum
     * @param currentPage 当前页
     * @param pageSize    页大小
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/messages/{type}")
    @Operation(summary = "获取某种类型通知")
    public ResultVo<NoticeVO> getNotices(@PathVariable String type,
                                            @RequestParam(defaultValue = "1")  Integer currentPage,
                                            @RequestParam(defaultValue = "10") Integer pageSize){
        NoticeVO noticeVO = noticeService.getNotices(type,currentPage,pageSize);
        return ResultVo.ok(noticeVO);
    }


    /**
     * 清除所有已读
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "/clearUnread")
    @Operation(summary = "清除所有已读")
    public ResVo clearAllUnread(){
        noticeService.clearAllUnread();
        return ResVo.ok(true);
    }






}