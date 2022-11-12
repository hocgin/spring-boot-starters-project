DROP TABLE IF EXISTS `boot_http_log`;
CREATE TABLE `boot_http_log`
(
    id                BIGINT AUTO_INCREMENT,
    uri               VARCHAR(2048) NOT NULL DEFAULT 'unknown'
        COMMENT '请求路径',
    request_method    VARCHAR(32)   NOT NULL DEFAULT 'unknown'
        COMMENT '请求方式',
    request_body      TEXT          NULL     DEFAULT NULL
        COMMENT '请求体',
    request_headers   TEXT          NULL     DEFAULT NULL
        COMMENT '请求头(JSON)',
    response_headers  TEXT          NULL     DEFAULT NULL
        COMMENT '响应头(JSON)',
    response_body     TEXT          NULL     DEFAULT NULL
        COMMENT '响应体',
    title             VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '名称',
    code              VARCHAR(128)  NOT NULL DEFAULT 'unknown'
        COMMENT '业务编码',
    remark            VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '备注',
    attach            VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '扩展数据',
    fail_reason       VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '失败原因',
    direction         VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '出入方向 [out=>出;in=>入]',
    be_caller         VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '响应系统',
    caller            VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '请求系统',
    status            VARCHAR(32)   NOT NULL DEFAULT 'executing'
        COMMENT '请求状态[executing=>执行中;success=>成功;fail=>失败]',
    done_at           DATETIME(6)
        COMMENT '完成时间',
    request_ip        VARCHAR(32)
        COMMENT '请求IP',
    --
    `created_at`      DATETIME(6)   NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6)   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 请求日志表';
