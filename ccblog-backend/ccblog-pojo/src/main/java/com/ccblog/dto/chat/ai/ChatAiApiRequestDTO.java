package com.ccblog.dto.chat.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 调用官方接口请求类
 * 可以参考官方文档
 * https://www.xfyun.cn/doc/spark/%E6%8E%A8%E7%90%86%E6%9C%8D%E5%8A%A1-http.html#_2-%E6%8E%A5%E5%8F%A3%E8%AF%B7%E6%B1%82
 * @author czc
 * @date 2025/10/28
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatAiApiRequestDTO implements Serializable {

    /**
     * 模型名称，指定使用哪个模型进行对话生成
     */
    private String model;

    /**
     * 消息列表，包含对话的历史消息
     */
    private List<Message> messages;

    /**
     * 是否以流式方式返回结果，用于实时获取生成内容
     */
    private Boolean stream;

    /**
     * 温度参数，用于控制生成文本的随机性
     * 取值为[0,1],默认值为0.7
     */
    private Float temperature;

    /**
     * 最大生成 token 数，限制生成文本的长度
     * 取值为[1,32768]，默认值为2048
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 停止序列，生成过程中遇到这些序列将停止生成
     */
    private List<String> stop;

    /**
     * 关闭联网检索
     * true/false 默认 true
     */
    @JsonProperty("search_disable")
    private Boolean searchDisable;


    /* ================= 内部静态类 ================= */
    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message implements Serializable {
        /**
         * user表示用户的问题，assistant表示AI的回复,system表示对话背景信息
         * 比如一个多轮交互:
         * [
         *     {"role": "system", "content": "你是一个乐于助人的AI助手。"},
         *     {"role": "user", "content": "你好，你是谁？"},
         *     {"role": "assistant", "content": "我是AI助手，有什么可以帮您？"},
         *     {"role": "user", "content": "你会做什么？"}
         * ]
         */
        private String role;

        /**
         * 消息内容，包含发送者所传递的文本信息
         */
        private String content;

    }
}
