package com.ccblog.service.notice.helper;

import com.ccblog.dto.comment.CommentInfoDTO;
import com.ccblog.dto.notice.NoticeDetailDTO;
import com.ccblog.enumeration.notice.NoticeTargetTypeEnum;
import com.ccblog.mapper.comment.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 通知工具类
 * @author czc
 * @date 2025/11/27
 */
@Component
public class NoticeHelper {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 给评论通知加下文信息到扩展字段
     */
    public void addContext(List<NoticeDetailDTO> noticeDetailDTOS){
        // 查出需要查询所属文章/专栏的评论
        Set<Long> ids = new HashSet<>(); // 某个评论所在的索引位置(可能重复,因为可能有多个人点赞同一条评论)
        Map<Long,List<Integer>> idxMap = new HashMap<>();
        for(int i=0;i<noticeDetailDTOS.size();i++){
            NoticeDetailDTO dto = noticeDetailDTOS.get(i);
            if(dto.getRelatedType()== NoticeTargetTypeEnum.COMMENT.getType()){ // 主体为评论
                Long commentId =dto.getRelatedId();
                ids.add(commentId);// 评论id
                idxMap.computeIfAbsent(commentId, k -> new ArrayList<>()).add(i); // 记录评论索引位置.  不存在就创建,存在就追加
            }
        }
        if(ids.isEmpty())return; // 没有需要处理的
        List<CommentInfoDTO> commentInfoDTOS = commentMapper.getCommInfoByCommId(ids.stream().toList());
        for(CommentInfoDTO commentInfoDTO:commentInfoDTOS){
            for(int idx:idxMap.get(commentInfoDTO.getCommentId())){
                NoticeDetailDTO dto = noticeDetailDTOS.get(idx);
                dto.setExtendInfo(commentInfoDTO.getTitle());
                dto.setExtendId(commentInfoDTO.getDocId());
            }
        }
    }
}