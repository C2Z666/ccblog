package com.ccblog.controller.user;

import com.ccblog.dto.common.ReportDetailDTO;
import com.ccblog.enumeration.StatusEnum;
import com.ccblog.service.common.CommonService;
import com.ccblog.utils.AliOssUtil;
import com.ccblog.vo.common.ReportTypeVO;
import com.ccblog.vo.global.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/common")
@Tag(name = "通用接口",description = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private CommonService commonService;

    /**
     * 上传图片到阿里云
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public ResVo<URL> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            URL url = aliOssUtil.upload(file);
            return ResVo.ok(url);
        }catch (IOException e){
            log.info("文件上传失败:{}",e);
        }
        catch (IllegalArgumentException e) { // ← 捕获配置缺失等运行时异常
            log.error("文件上传失败(OSS配置异常)", e);
            return ResVo.fail(StatusEnum.OSS_ARGUMENT_FAILED, e.getMessage());
        }
        return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED); // 失败
    }

    /**
     * 获取举报条目
     * @param type  类型 @ReportTargetTypeEnum
     * @return
     */
    @GetMapping("/report/list")
    @Operation(summary = "获取举报条目")
    public ResVo<List<ReportTypeVO>> getReportType(int type){
        List<ReportTypeVO> reportTypeVOS = commonService.getAllReportItems(type);
        return ResVo.ok(reportTypeVOS);
    }

    /**
     * 举报操作
     * @param reportDetailDTO  举报信息
     * @return
     */
    @PostMapping("/report/save")
    @Operation(summary = "举报")
    public ResVo report(@RequestBody ReportDetailDTO reportDetailDTO){
        commonService.saveReport(reportDetailDTO);
        return ResVo.ok(true);
    }
}
