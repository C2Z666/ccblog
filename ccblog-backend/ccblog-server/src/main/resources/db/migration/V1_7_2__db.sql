-- V1.7.2__
-- 添加用户实名、日志追踪等功能,方便追踪
-- 添加举报表格,并且初始化基本类型

-- 1. 举报类别表
CREATE TABLE report_type (
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reason       VARCHAR(32)  NOT NULL COMMENT '类别标识',
    target_mask  INT UNSIGNED NOT NULL COMMENT '位掩码：1文章 2评论 4用户',
    weight       INT          NOT NULL DEFAULT 1000,
    enabled      TINYINT(1)   NOT NULL DEFAULT 1,
    create_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_reason (reason),
    KEY idx_mask_weight (target_mask, weight)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '举报类别表';

-- 2. 举报记录表
CREATE TABLE report_record (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reporter_uid  INT UNSIGNED NOT NULL COMMENT '举报人',
    target_type   TINYINT(4)   NOT NULL COMMENT '1文章 2评论 4用户',
    target_id     BIGINT UNSIGNED NOT NULL COMMENT '被举报对象id',
    reason_id     INT UNSIGNED NOT NULL COMMENT '举报类型id',
    reason_text   TEXT         COMMENT '补充文字',
    proof_urls    JSON         COMMENT '证据图片数组',
    status        TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '0未处理 1已处理',
    result_action VARCHAR(32)  COMMENT '0忽略 1接受 2拒绝',
    result_note   VARCHAR(255) COMMENT '管理员备注',
    auditor_id    INT UNSIGNED COMMENT '处理管理员',
    create_time   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_reporter (reporter_uid),
    KEY idx_target (target_type, target_id),
    KEY idx_status (status, create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '举报记录表';

-- 3. 初始化常用举报类别 1文章 2评论 4用户
INSERT INTO report_type (reason, target_mask, weight, enabled)
VALUES
('政治敏感',    1|2,   10, 1),       --   文章/评论
('违法犯罪',    1|2|4, 20, 1),       --   全对象
('色情低俗',    1|2|4, 30, 1),       --   全对象
('引导私下交易', 1,     40, 1),       --   用户
('发布不当信息', 1,     40, 1),       --   用户
('抄袭剽窃',    1,     40, 1),       --   文章
('人身攻击',    2|4,   50, 1),       --   评论/用户
('垃圾营销',    1,     60, 1),       --   文章
('广告引流',    1|2,   70, 1),       --   文章/评论
('侵犯权益',    1,     70, 1),       --   用户
('不实信息',    1|2,   80, 1),       --   文章/评论
('其他',       1|2|4, 999,1);       --   全对象