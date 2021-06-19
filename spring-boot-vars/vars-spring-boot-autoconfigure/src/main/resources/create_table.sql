DROP TABLE IF EXISTS `boot_task_info`;
CREATE TABLE `boot_task_info`
(
    id         BIGINT AUTO_INCREMENT,
    var_code   VARCHAR(64)  NOT NULL
        COMMENT '变量码',
    # ---
    created_at TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    creator    BIGINT
        COMMENT '创建人',
    UNIQUE KEY (var_code),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 变量配置表';
