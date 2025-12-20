package com.ccblog.mapper.common;

import com.ccblog.entity.comment.CommentCount;
import com.ccblog.entity.common.ReportRecord;
import com.ccblog.vo.common.ReportTypeVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 通用相关查询
 * @author czc
 * @date 2025/12/20
 */
@Mapper
public interface CommonMapper {

    /**
     * 获取所有此类别的举报类别
     * @return
     */
    @Select("SELECT id,reason FROM report_type WHERE target_mask&#{type} ORDER BY weight")
    List<ReportTypeVO> getAllReportItems(int type);

    /**
     * 添加一条举报信息
     * @param reportRecord
     */
    void saveReportRecode(ReportRecord reportRecord);
}