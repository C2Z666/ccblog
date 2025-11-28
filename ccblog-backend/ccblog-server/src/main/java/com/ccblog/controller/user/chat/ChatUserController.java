package com.ccblog.controller.user.chat;

import com.ccblog.annotation.Permission;
import com.ccblog.dto.chat.user.ChatUserItemDTO;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.chat.ChatUserService;
import com.ccblog.vo.chat.ChatUserCursorVO;
import com.ccblog.vo.chat.ChatUserInfoVO;
import com.ccblog.vo.chat.ChatUserNewSessionVO;
import com.ccblog.vo.chat.ChatUserSessionCursorVO;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户聊天模块
 * @author czc
 * @date 2025/10/19
 */
@Slf4j
@Tag(name="用户聊天接口",description = "用户聊天相关接口")
@RequestMapping("/chat/user") // 这个只管http
@RestController
public class ChatUserController {
    @Autowired
    private ChatUserService chatUserService;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 获取用户会话列表
     * @param cursor     最后一条时间信息
     * @param cursorId   最后一条消息会话消息id
     * @param limit      数量
     * @return           游标用户会话列表
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/session")
    @Operation(summary = "获取聊天历史")
    public ResVo<ChatUserSessionCursorVO> getChatSession(@RequestParam(name = "cursor",required = false) LocalDateTime cursor,
                                                         @RequestParam(name = "cursorId",required = false) Long cursorId,
                                                         @RequestParam(name = "limit",defaultValue = "10") Integer limit){
        ChatUserSessionCursorVO chatUserCursorVO = chatUserService.getChatSession(cursor,cursorId,limit);
        return ResVo.ok(chatUserCursorVO);
    }

    /**
     * 获取用户聊天记录
     * @param peerId     发送者id
     * @param cursor     最后一条时间信息
     * @param cursorSeq  最后一条消息seq
     * @param limit      数量
     * @return           游标消息历史记录
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/history")
    @Operation(summary = "获取聊天历史")
    public ResVo<ChatUserCursorVO> getChatHistory(@RequestParam(name = "peerId") Long peerId,
                                                  @RequestParam(name = "cursor",required = false) LocalDateTime cursor,
                                                  @RequestParam(name = "cursorSeq",required = false) Long cursorSeq,
                                                  @RequestParam(name = "limit",defaultValue = "10") Integer limit){
        ChatUserCursorVO chatUserCursorVO = chatUserService.getChatHistory(peerId,cursor,cursorSeq,limit);
        return ResVo.ok(chatUserCursorVO);
    }

    /**
     * 获取聊天双方所需的基本信息(用户名,头像...),前端有尽量用前端的
     * 准备连接操作
     * @param peerId  对方id
     * @return
     */
    @GetMapping(path = "/chatInfo")
    @Operation(summary = "获取聊天所需基本信息")
    public ResultVo<ChatUserInfoVO> getChatInfo(@RequestParam(name = "peerId") Long peerId){
        ChatUserInfoVO chatUserInfoVO = chatUserService.getChatUserInfo(peerId);
        return ResultVo.ok(chatUserInfoVO);
    }

    /**
     * 消息操作 删除
     * @param chatUserItemDTO  消息体
     * @return
     */
    @DeleteMapping(path = "/delete")
    @Operation(summary = "删除消息")
    public ResVo<ChatUserNewSessionVO> deleteMsg(ChatUserItemDTO chatUserItemDTO){
        ChatUserNewSessionVO newSession = chatUserService.deleteMessage(chatUserItemDTO);
        return ResVo.ok(newSession);
    }

    /**
     * 会话操作 删除
     * @return
     */
    @PostMapping(path = "/unread/clear")
    @Operation(summary = "清除所有已读")
    public ResVo cleanAllUnread(){
        chatUserService.clearAllUnread();
        return ResVo.ok(true);
    }

    /**
     * 消息操作 撤回
     * @param chatUserItemDTO  消息体
     * @return
     */
    @GetMapping(path = "/recall")
    @Operation(summary = "撤回消息")
    public ResVo<ChatUserNewSessionVO> recallMsg(ChatUserItemDTO chatUserItemDTO){
        ChatUserNewSessionVO newSession = chatUserService.recallMessage(chatUserItemDTO);
        // 回执:给接受者推送消息seq方便渲染撤回
        simpMessagingTemplate.convertAndSendToUser(
                chatUserItemDTO.getRecvUserId().toString(),     // 对方 UID
                "/chat/user",              // 他订阅目的地
                Map.of("seq", chatUserItemDTO.getSeq(),
                        "type", "recall"));                  // 负载
        return ResVo.ok(newSession);
    }

    /**
     * 会话操作 删除
     * @param type      类型
     * @param sessionId 会话id
     * @return
     */
    @PostMapping(path = "/session/operate/{type}")
    @Operation(summary = "会话操作")
    public ResVo operateSession(@PathVariable(name = "type") Integer type,
                                @RequestParam(name = "sessionId") Long sessionId){
        chatUserService.operateSession(type,sessionId);
        return ResVo.ok(true);
    }


    /**
     * 获取连接所需ticket
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/ticket")
    @Operation(summary = "获取ticket")
    public ResVo<String> getTicket(){
        String token = chatUserService.getTicket();
        if(token==null){ // 无权限
            return ResVo.fail(StatusEnum.FORBID_ERROR_MIXED);
        }
        return ResVo.ok(token);
    }

    /**
     * 推送消息给另一个用户
     * @param chatUserItemDTO 消息体
     * @param recvUserId
     * @param principal
     */
    @MessageMapping("/private/{recvUserId}")   // 发送者把帧发到 /app/private/对方ID
    @SendToUser("/chat/user")               // 同时回执给发送者
    @Operation(summary = "转发消息")
    public void privateChat(ChatUserItemDTO chatUserItemDTO,
                            @DestinationVariable(value = "recvUserId") Long recvUserId, // 路径变量toUserId
                            Principal principal) {
        // 存储消息
        Long sendUserId = Long.parseLong(principal.getName());
        chatUserItemDTO = chatUserService.saveChatInfo(chatUserItemDTO,sendUserId,recvUserId);
        // 回执:给发送者推消息seq
        simpMessagingTemplate.convertAndSendToUser(
                chatUserItemDTO.getSendUserId().toString(),     // 自己 UID
                "/chat/user",              // 订阅目的地
                Map.of("seq", chatUserItemDTO.getSeq(),
                        "type", "receipt"));                  // 负载
        // 回执:给接受者推完整消息体
        simpMessagingTemplate.convertAndSendToUser(
                recvUserId.toString(),     // 对方 UID
                "/chat/user",              // 他订阅目的地
                Map.of("type","msg",
                        "data",chatUserItemDTO));                  // 负载
    }

    /**
     * ticket续期
     */
    @MessageMapping("/ping")
    @Operation(summary = "ticket续期接口")
    public void ping(Principal principal) {
        ;
    }


}