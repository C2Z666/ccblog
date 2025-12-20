package com.ccblog.service.common;

import com.ccblog.dto.common.ReportDetailDTO;
import com.ccblog.vo.common.ReportTypeVO;

import java.util.List;

/**
 * 通用服务
 * @author czc
 * @date 2025/12/20
 */
public interface CommonService {
    /**
     * 获取需要的举报信息
     * @param type
     * @return
     */
    List<ReportTypeVO> getAllReportItems(int type);

    /**
     * 保存举报信息
     * @param reportDetailDTO
     */
    void saveReport(ReportDetailDTO reportDetailDTO);
}