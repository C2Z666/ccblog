package com.ccblog.controller.user.comment;

import com.ccblog.annotation.Permission;
import com.ccblog.dto.comment.CommentItemDTO;
import com.ccblog.dto.comment.NewCommentDTO;
import com.ccblog.enumeration.user.UserRole;
import com.ccblog.service.comment.CommentService;
import com.ccblog.vo.comment.CommentCursorVO;
import com.ccblog.vo.global.ResVo;
import com.ccblog.vo.global.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 评论相关内容
 * @author czc
 * @date 2025-10-03
 */
@Slf4j
@Tag(name="评论接口",description = "评论相关接口")
@RequestMapping("/comment")
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 保存评论
     * @param newCommentDTO
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "/save")
    @Operation(summary = "保存/更新评论")
    public ResVo<CommentItemDTO> saveComment(@RequestBody NewCommentDTO newCommentDTO) {
        CommentItemDTO commentItemDTO= commentService.saveComment(newCommentDTO);
        return ResVo.ok(commentItemDTO);
    }

    /**
     * 评论点赞/点踩等
     * @param commentId
     * @param opeatationType
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "/commentOperation")
    @Operation(summary = "评论操作")
    public ResVo<Boolean> operateComment(@RequestParam(name = "commentId") Long commentId,
                                         @RequestParam(name = "type") Integer opeatationType){
        commentService.operateComment(commentId,opeatationType);
        return ResVo.ok(true);
    }

    /**
     * 游标方式获取评论
     * @param parentId 需要展开的评论id
     * @param cursor   展开的最后一条时间
     * @param cursorId 展开的最后一条的评论id
     * @param limit    获取数量
     * @return
     */
    @GetMapping(path = "/getCommentCursor")
    @Operation(summary = "游标获取评论")
    public ResultVo<CommentCursorVO> getCursorComments(@RequestParam(name = "articleId") Long articleId,
                                                       @RequestParam(name = "parentId") Long parentId,
                                                       @RequestParam(name = "cursor",required = false) LocalDateTime cursor,
                                                       @RequestParam(name = "cursorId",required = false) Long cursorId,
                                                       @RequestParam(name = "limit",defaultValue = "10") Integer limit){
        CommentCursorVO commentCursorVO = commentService.getCursorComments(articleId,parentId,cursor,cursorId,limit);
        return ResultVo.ok(commentCursorVO);
    }


}