-- V1.5.0__
-- 聊天模块
-- 创建聊天模块必要表格

-- 1. 用户聊天记录
-- 加上函数式索引idx_dialog,方便用两人id联合索引查询,注意这个实际上是一条索引
CREATE TABLE chat_user_history (
    id            int(10) unsigned AUTO_INCREMENT PRIMARY KEY,
    seq           int(10) unsigned NOT NULL DEFAULT 1 COMMENT '对话内消息序号',
    sender_id     int(10) unsigned NOT NULL,
    receiver_id   int(10) unsigned NOT NULL,
    content       text             NOT NULL,
    msg_type      tinyint          NOT NULL DEFAULT 0 COMMENT '0文本 1图片 2文件 3语音 4视频',
    status        tinyint          NOT NULL DEFAULT 0 COMMENT '0正常 1发送者撤回 2系统删除',
    read_flag     tinyint          NOT NULL DEFAULT 0 COMMENT '0未读 1已读',
    deleted_by_s  tinyint          NOT NULL DEFAULT 0 COMMENT '0正常 1发送方删除',
    deleted_by_r  tinyint          NOT NULL DEFAULT 0 COMMENT '0正常 1接收方删除',
    create_time   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_sender_seq (sender_id, seq),
    KEY idx_receiver_seq (receiver_id, seq),
    KEY idx_dialog ( (LEAST(sender_id, receiver_id)),
                     (GREATEST(sender_id, receiver_id)),
                     seq DESC )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户聊天记录';

-- 2. 用户会话表
CREATE TABLE chat_user_session (
    id            int(10) unsigned AUTO_INCREMENT PRIMARY KEY,
    user_id       int(10) unsigned NOT NULL,
    peer_id       int(10) unsigned NOT NULL,
    snapshot      varchar(200)     NOT NULL DEFAULT '' COMMENT '最新消息前50字',
    last_msg_time timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unread_count  int(10) unsigned NOT NULL DEFAULT 0,
    is_top        tinyint(1)       NOT NULL DEFAULT 0 COMMENT '0不置顶 1置顶',
    is_mute       tinyint(1)       NOT NULL DEFAULT 0 COMMENT '0接收通知 1关闭',
    is_delete     tinyint(1)       NOT NULL DEFAULT 0 COMMENT '0正常 1已删除',
    create_time   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_peer (user_id, peer_id),
    KEY idx_user_time (user_id, is_top DESC, last_msg_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户会话表';

-- 3. AI 聊天记录
CREATE TABLE chat_ai_history (
    id          int(10) unsigned AUTO_INCREMENT PRIMARY KEY,
    session_id  int(10) unsigned NOT NULL,
    seq         int(10) unsigned NOT NULL DEFAULT 1,
    parent_id   varchar(32)      NOT NULL DEFAULT '0',
    user_id     int(10) unsigned NOT NULL DEFAULT 0,
    sender      tinyint          NOT NULL COMMENT '0用户 1AI',
    ai_type     tinyint          NOT NULL DEFAULT 0 COMMENT '0ccLLM 1chatgpt3.5 2chatgpt4 3讯飞',
    content     text             NOT NULL,
    deleted     tinyint(1)       NOT NULL DEFAULT 0,
    create_time timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_session_seq (session_id, seq)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 聊天记录';

-- 4. AI 会话汇总
CREATE TABLE chat_ai_session (
    session_id  int(10) unsigned PRIMARY KEY,
    user_id     int(10) unsigned NOT NULL,
    title       varchar(120)     NOT NULL DEFAULT '新会话',
    deleted     tinyint(1)       NOT NULL DEFAULT 0,
    create_time timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_user_time (user_id, update_time DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 会话汇总';