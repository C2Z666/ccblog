package com.ccblog.controller.user.chat;

import cn.hutool.json.JSONObject;
import com.ccblog.annotation.Permission;
import com.ccblog.dto.chat.ai.ChatAiRequestDTO;
import com.ccblog.entity.chat.ChatAiSession;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.chat.ChatAiService;
import com.ccblog.vo.chat.ChatAiCursorVO;
import com.ccblog.vo.chat.ChatAiSessionCursorVO;
import com.ccblog.vo.global.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * 用户ai聊天接口
 * @author czc
 * @date 2025/10/28
 */
@Slf4j
@Tag(name="用户AI聊天接口",description = "用户AI聊天相关接口")
@RequestMapping("/chat/ai")
@RestController
public class ChatAiController {
    @Autowired
    private ChatAiService chatAiService;

    /**
     * 用户调用模型
     * @param userRequest 用户提交请求
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "用户调用模型")
    @PostMapping(value = "/answer")
    public Flux<ServerSentEvent<JSONObject>> getAiAnswer(@RequestBody ChatAiRequestDTO userRequest) {
        return chatAiService.getAnswer(userRequest);
    }

    /**
     * 获取用户AI聊天历史
     * @param sessionId 会话id
     * @param cursor    游标时间
     * @param cursorSeq 游标seq
     * @param limit     数量
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "获取聊天记录")
    @GetMapping(value = "/history")
    public ResVo<ChatAiCursorVO> getChatHistory(@RequestParam(name = "sessionId") Long sessionId,
                                                @RequestParam(name = "cursor",required = false) LocalDateTime cursor,
                                                @RequestParam(name = "cursorSeq",required = false) Long cursorSeq,
                                                @RequestParam(name = "limit",defaultValue = "10") int limit){
        ChatAiCursorVO chatHistory =  chatAiService.getChatHistory(sessionId,cursor,cursorSeq,limit);
        return ResVo.ok(chatHistory);
    }

    /**
     * 获取用户AI聊天会话
     * @param cursorId  游标sessionId
     * @param limit     数量
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "获取聊天会话")
    @GetMapping(value = "/sessions")
    public ResVo<ChatAiSessionCursorVO> getChatHistory(@RequestParam(name = "cursorId",required = false) Long cursorId,
                                                @RequestParam(name = "limit",defaultValue = "10") int limit){
        ChatAiSessionCursorVO chatSession =  chatAiService.getChatSession(cursorId,limit);
        return ResVo.ok(chatSession);
    }

    /**
     * 用户删除会话
     * @param sessionId 会话id
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "用户删除会话")
    @DeleteMapping(value = "/session/delete")
    public ResVo deleteSession(@RequestParam(name = "sessionId") Long sessionId) {
        chatAiService.deleteSession(sessionId);
        return ResVo.ok(true);
    }

    /**
     * 用户重命名会话
     * @param sessionId 会话id
     * @param title     新标题
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "用户重命名会话")
    @PutMapping(value = "/session/rename")
    public ResVo renameSession(@RequestParam(name = "sessionId") Long sessionId,
                               @RequestParam(name = "title") String title) {
        chatAiService.renameSession(sessionId,title);
        return ResVo.ok(true);
    }

    /**
     * 用户删除聊天记录
     * @param sessionId 会话id
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @Operation(summary = "用户删除聊天记录")
    @DeleteMapping(value = "/message/delete")
    public ResVo deleteMessage(@RequestParam(name = "sessionId") Long sessionId,
                            @RequestParam(name = "seq") Long seq) {
        chatAiService.deleteMessage(sessionId,seq);
        return ResVo.ok(true);
    }
}