DROP TABLE IF EXISTS `boot_config_scope`;
CREATE TABLE `boot_config_scope`
(
    `id`              BIGINT AUTO_INCREMENT
        COMMENT 'ID',
    `scope`           VARCHAR(64)  NOT NULL
        COMMENT '域',
    `title`           VARCHAR(128) NOT NULL
        COMMENT '标题',
    `remark`          VARCHAR(255) NOT NULL
        COMMENT '备注',
    --
    `created_at`      DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6)  NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    UNIQUE KEY (`scope`),
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '[mbp|配置] 配置域表';

DROP TABLE IF EXISTS `boot_config_item`;
CREATE TABLE `boot_config_item`
(
    `id`              BIGINT AUTO_INCREMENT
        COMMENT 'ID',
    `scope_id`        BIGINT      NOT NULL
        COMMENT '域',
    `name`            VARCHAR(64) NOT NULL
        COMMENT '键',
    `type`            VARCHAR(128)
        COMMENT '类型',
    `default_value`   VARCHAR(1024)
        COMMENT '默认值',
    `readable`        tinyint(1)  NOT NULL DEFAULT 0
        COMMENT '是否可读',
    `writable`        tinyint(1)  NOT NULL DEFAULT 0
        COMMENT '是否可写',
    `nullable`        tinyint(1)  NOT NULL DEFAULT 0
        COMMENT '是否可空',
    --
    `created_at`      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6) NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    UNIQUE KEY (`scope_id`, `name`),
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '[mbp|配置] 域的配置项表';

DROP TABLE IF EXISTS `boot_config_value`;
CREATE TABLE `boot_config_value`
(
    `id`              BIGINT AUTO_INCREMENT
        COMMENT 'ID',
    `item_id`         BIGINT      NOT NULL
        COMMENT '配置项',
    `ref_id`          BIGINT      NOT NULL
        COMMENT '关联对象',
    `value`           VARCHAR(1024)
        COMMENT '值',
    --
    `created_at`      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6) NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    UNIQUE KEY (`item_id`, `ref_id`),
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT = '[mbp|配置] 配置项的值表';
