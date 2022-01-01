DROP TABLE IF EXISTS `model_entity`;
CREATE TABLE `model_entity`
(
    `id`              BIGINT AUTO_INCREMENT
        COMMENT 'ID',
    --
    `tenant_id`       BIGINT      NOT NULL DEFAULT 0
        COMMENT '租户',
    `deleted_at`      DATETIME(6)          DEFAULT NULL
        COMMENT '删除时间',
    `deleter`         BIGINT               DEFAULT NULL
        COMMENT '删除者',
    `created_at`      DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        COMMENT '创建时间',
    `creator`         BIGINT
        COMMENT '创建者',
    `last_updated_at` DATETIME(6) NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
        COMMENT '更新时间',
    `last_updater`    BIGINT
        COMMENT '更新者',

    PRIMARY KEY (`id`)
);
