DROP TABLE IF EXISTS `boot_persistence_message`;
CREATE TABLE `boot_persistence_message`
(
    id              BIGINT AUTO_INCREMENT,
    group_sn        VARCHAR(64)       NOT NULL
        COMMENT '消息组编号',
    destination     VARCHAR(64)       NOT NULL
        COMMENT '消息目的地',
    payload         TEXT
        COMMENT '消息体',
    headers         TEXT
        COMMENT '消息头',
    published       TINYINT DEFAULT 0 NOT NULL
        COMMENT '消息状态[0=>为准备状态;1=>已发布]',
    prepared_at     DATETIME(6)       NOT NULL,
    created_at      DATETIME(6)       NOT NULL,
    last_updated_at DATETIME(6)       NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 持久化消息表';
