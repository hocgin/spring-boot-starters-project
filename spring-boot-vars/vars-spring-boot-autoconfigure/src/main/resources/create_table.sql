DROP TABLE IF EXISTS `boot_vars_config`;
CREATE TABLE `boot_vars_config`
(
    id                BIGINT AUTO_INCREMENT,
    title             VARCHAR(125) NOT NULL DEFAULT 'unknown'
        COMMENT '标题',
    var_key           VARCHAR(64)  NOT NULL
        COMMENT '变量名',
    var_value         TEXT
        COMMENT '变量内容',
    remark            VARCHAR(255)
        COMMENT '备注',
    enabled           BIT          NOT NULL DEFAULT 1
        COMMENT '启用状态',
    # ---
    created_at        TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    creator           BIGINT
        COMMENT '创建人',
    `last_updated_at` DATETIME(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',
    UNIQUE KEY (var_key),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 变量配置表';
