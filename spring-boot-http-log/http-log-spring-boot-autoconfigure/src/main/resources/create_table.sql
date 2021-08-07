DROP TABLE IF EXISTS `boot_http_log`;
CREATE TABLE `boot_http_log`
(
    id               BIGINT AUTO_INCREMENT,
    uri              VARCHAR(2048) NOT NULL DEFAULT 'unknown'
        COMMENT '请求路径',
    request_method   VARCHAR(32)   NOT NULL DEFAULT 'unknown'
        COMMENT '请求方式',
    request_body     TEXT          NULL     DEFAULT NULL
        COMMENT '请求体',
    request_headers  TEXT          NULL     DEFAULT NULL
        COMMENT '请求头(JSON)',
    response_headers TEXT          NULL     DEFAULT NULL
        COMMENT '响应头(JSON)',
    response_body    TEXT          NULL     DEFAULT NULL
        COMMENT '响应体',
    title            VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '名称',
    code             VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '业务编码',
    remark           VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '备注',
    attach           VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '扩展数据',
    fail_reason      VARCHAR(1024) NULL     DEFAULT NULL
        COMMENT '失败原因',
    direction        VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '出入方向 [out=>出;in=>入]',
    be_caller        VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '响应系统',
    caller           VARCHAR(64)   NOT NULL DEFAULT 'unknown'
        COMMENT '请求系统',
    status           VARCHAR(32)   NOT NULL DEFAULT 'executing'
        COMMENT '请求状态[executing=>执行中;success=>成功;fail=>失败]',
    done_at          TIMESTAMP(6)
        COMMENT '完成时间',
    created_at       TIMESTAMP(6)  NOT NULL
        COMMENT '创建时间',
    creator          VARCHAR(32)
        COMMENT '创建人',
    creator_ip       VARCHAR(32)
        COMMENT '创建人',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] HTTP 请求日志表';
