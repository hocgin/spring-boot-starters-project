DROP TABLE IF EXISTS `boot_change`;
CREATE TABLE `boot_change`
(
    id                BIGINT AUTO_INCREMENT,
    log_sn            VARCHAR(64) NOT NULL UNIQUE,
    ref_type          VARCHAR(32) NOT NULL
        COMMENT '业务类型: 如: 订单',
    ref_id            BIGINT      NOT NULL
        COMMENT '业务ID: 如: 订单ID',
    change_type       VARCHAR(8)  NOT NULL
        COMMENT '操作类型: insert->新增, modify->修改, delete->删除',
    --
    `created_at`      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6) NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 业务操作日志表';

DROP TABLE IF EXISTS `boot_change_field`;
CREATE TABLE `boot_change_field`
(
    id                BIGINT AUTO_INCREMENT,
    change_id         BIGINT       NOT NULL
        COMMENT '业务操作日志',
    field_name        VARCHAR(32)  NOT NULL
        COMMENT '字段名',
    field_remark      VARCHAR(255) NOT NULL
        COMMENT '字段备注',
    change_remark     VARCHAR(255) NOT NULL
        COMMENT '变更描述',
    before_value      VARCHAR(255) NOT NULL
        COMMENT '变更前',
    after_value       VARCHAR(255) NOT NULL
        COMMENT '变更后',
    --
    `created_at`      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6)  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 业务日志-字段变更记录表';
