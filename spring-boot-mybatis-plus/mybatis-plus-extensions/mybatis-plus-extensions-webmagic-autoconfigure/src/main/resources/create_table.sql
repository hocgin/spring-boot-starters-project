DROP TABLE IF EXISTS `boot_webmagic`;
CREATE TABLE `boot_webmagic`
(
    id                BIGINT AUTO_INCREMENT,
    `type`            VARCHAR(64) NOT NULL DEFAULT 'general'
        COMMENT '类型',
    `status`          VARCHAR(8)  NOT NULL DEFAULT 'progress'
        COMMENT '完成状态',
    `fail_reason`     VARCHAR(255)
        COMMENT '失败原因',
    `pull_url`        VARCHAR(255)
        COMMENT '拉取的地址',
    `pull_data`       JSON
        COMMENT '拉取的数据',
    `finished_at`     DATETIME(6) NULL
        COMMENT '完成时间',
    # ---
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
    COMMENT '[BOOT] 爬虫采集表';
