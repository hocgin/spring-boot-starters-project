DROP TABLE IF EXISTS `boot_user_config`;
CREATE TABLE `boot_user_config`
(
    `id`              BIGINT AUTO_INCREMENT
        COMMENT 'ID',
    `user_id`         BIGINT      NOT NULL
        COMMENT '用户ID',
    `scope`           VARCHAR(64) NOT NULL DEFAULT 'default'
        COMMENT '域,如: 项目编号',
    `code`            VARCHAR(64) NOT NULL
        COMMENT '配置项',
    `value`           VARCHAR(64) NOT NULL
        COMMENT '配置值',
    --
    `created_at`      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6) NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    UNIQUE KEY (`user_id`, `scope`, `code`),
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '[BOOT] 用户配置表';
