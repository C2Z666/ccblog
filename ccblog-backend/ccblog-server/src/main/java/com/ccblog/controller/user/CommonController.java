package com.ccblog.controller.user;

import com.ccblog.enumeration.StatusEnum;
import com.ccblog.utils.AliOssUtil;
import com.ccblog.vo.global.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

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
}
