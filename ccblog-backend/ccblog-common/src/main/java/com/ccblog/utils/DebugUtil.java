package com.ccblog.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * 一些debug工具
 * @author czc
 * @date 2025/11/12
 */
@Slf4j
public class DebugUtil {
    /**
     * 追加写入txt文件
     * @param path    txt路径
     * @param content 内容
     */
    public static void textWriterAdd(String path,String content){
        if(!path.endsWith(".txt")) return; // 只能写入txt
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path, true), StandardCharsets.UTF_8))) {
            writer.write(String.format(content));
            writer.newLine();          // 自动换行
            writer.flush();
        } catch (IOException e) {
            ;
//            log.error("【文件写version失败】version={}, file={}, e={}", content, logFile, e.getMessage());
        }
    }
}