package com.ccblog.dto.chat.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 调用官方接口返回类
 * @author czc
 * @date 2025/10/28
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatAiResponseDTO implements Serializable {
    /**
     * 响应ID，唯一标识这次对话生成的响应
     */
    private String id;

    /**
     * 模型生成的回复候选项列表，通常 1 条
     */
    private List<Choice> choices;

    /**
     * 响应生成时间的 Unix 时间戳（秒）
     */
    private Integer created;

    /**
     * 实际调用的模型名称
     */
    private String model;

    /**
     * 响应对象类型，固定值 "chat.completion"
     */
    private String object;

    /**
     * 服务层级，如 "default" / "premium"
     */
    @JsonProperty("service_tier")
    private String serviceTier;

    /**
     * 系统指纹/配置标识
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * Token 用量统计
     */
    private Usage usage;

    /* ================= 内部静态类 ================= */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice implements Serializable {
        /**
         * 结束原因：stop / length / content_filter / tool_calls
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 候选项在数组中的位置
         */
        private Integer index;

        /**
         * 消息内容对象
         */
        private Message message;

        /**
         * 流式返回片段
         */
        private Delta delta;

        /* -------------------------------------------------- */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Message implements Serializable {
            /**
             * 模型生成的回复文本
             */
            private String content;

            /**
             * 思考文本（深度思考模型）
             */
            @JsonProperty("reasoning_content")
            private String reasoningContent;

            /**
             * 联网检索信源结果（支持联网模型）
             */
            @JsonProperty("plugins_content")
            private List<PluginContent> pluginsContent;

            /**
             * 模型拒绝回答时的信息
             */
            private Object refusal;

            /* -------------- pluginsContent 内部对象 -------------- */
            public static class PluginContent implements Serializable {
                /**
                 * 插件名称，如 ifly_search
                 */
                private String name;

                /**
                 * 信源结果列表（结构同外层定义，可再细化）
                 */
                private List<SearchResult> results;

                public static class SearchResult implements Serializable {
                    private Integer index;
                    private String url;
                    private String title;
                }
            }
        }

        /**
         * 流式返回增量信息
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Delta implements Serializable {
            /**
             * 模型生成的增量文本内容（流式响应）
             */
            private String content;

            /**
             * 消息角色（可选，一般为 "assistant"）
             */
            private String role;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage implements Serializable {
        /**
         * 总token数
         */
        private Integer total_tokens;
    }


}