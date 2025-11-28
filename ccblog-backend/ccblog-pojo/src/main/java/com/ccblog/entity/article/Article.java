package com.ccblog.entity.article;

import com.ccblog.entity.BaseInfo;
import lombok.*;

import java.io.Serializable;

/**
 * @author czc
 * @date 2025-09-27
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Article extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /* 作者用户ID */
    private Long userId;
    /* 文章类型：1-博文，2-问答 */
    private Integer articleType;
    /* 文章标题 */
    private String title;
    /* 短标题 */
    private String shortTitle;
    /* 文章头图URL */
    private String picture;
    /* 文章摘要 */
    private String summary;
    /* 类目ID */
    private Long categoryId;
    /* 来源：1-转载，2-原创，3-翻译 */
    private Integer source;
    /* 原文链接 */
    private String sourceUrl;
    /* 版本号 */
    private Integer version;
    /* 官方状态：0-非官方，1-官方 */
    private Integer officalStat;
    /* 置顶状态：0-不置顶，1-置顶 */
    private Integer toppingStat;
    /* 加精状态：0-不加精，1-加精 */
    private Integer creamStat;
    /* 发布状态：0-未发布，1-已发布 */
    private Integer status;
    /* 是否删除：0-正常，1-已删除 */
    private Integer deleted;
}