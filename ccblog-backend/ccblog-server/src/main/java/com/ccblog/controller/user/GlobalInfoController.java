package com.ccblog.controller.user;

import com.ccblog.vo.global.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: tech-pai
 * @description:
 * @author: XuYifei
 * @create: 2024-07-05
 */

@RestController
@Tag(name = "全局信息接口")
@RequestMapping("/global")
public class GlobalInfoController {
    /**
     * 用于单独提供一个接口，用于前端获取全局信息
     * @return
     */
    @Operation(summary = "查询全局信息")
    @GetMapping("info")
    public ResultVo<String> getGlobalInfo() {
        return ResultVo.ok("ok");
    }
}
