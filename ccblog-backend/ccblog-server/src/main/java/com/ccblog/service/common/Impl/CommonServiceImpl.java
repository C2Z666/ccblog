package com.ccblog.service.common.Impl;

import com.ccblog.context.ReqInfoContext;
import com.ccblog.dto.common.ReportDetailDTO;
import com.ccblog.entity.common.ReportRecord;
import com.ccblog.enumeration.common.ReportTargetTypeEnum;
import com.ccblog.mapper.common.CommonMapper;
import com.ccblog.service.common.CommonService;
import com.ccblog.vo.common.ReportTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用实现类
 * @author czc
 * @date 2025/12/20
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 获取举报信息
     * @param type
     * @return
     */
    public List<ReportTypeVO> getAllReportItems(int type) {
        List<ReportTypeVO> reportTypeVOs = commonMapper.getAllReportItems(type);
        return reportTypeVOs;
    }

    /**
     * 保存举报信息
     * @param reportDetailDTO
     */
    public void saveReport(ReportDetailDTO reportDetailDTO) {
        ReportRecord reportRecord = new ReportRecord();
        BeanUtils.copyProperties(reportDetailDTO,reportRecord);
        reportRecord.setReporterUid(ReqInfoContext.getReqInfo().getUserId()); // 举报用户
        commonMapper.saveReportRecode(reportRecord);

    }
}