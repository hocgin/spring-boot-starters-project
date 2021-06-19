DROP TABLE IF EXISTS `boot_oauth_client`;
CREATE TABLE `boot_oauth_client`
(
    id              BIGINT AUTO_INCREMENT,
    title           VARCHAR(32)  NOT NULL
        COMMENT '应用名称',
    client_id       VARCHAR(128) NOT NULL
        COMMENT 'client id',
    client_secret   VARCHAR(128) NOT NULL
        COMMENT 'client secret',
    type            VARCHAR(8)   NOT NULL
        COMMENT '类型: youtube',
    access_token    VARCHAR(255)
        COMMENT 'access token',
    refresh_token   VARCHAR(255)
        COMMENT 'refresh token',
    expiration_time BIGINT
        COMMENT '过期时间: ms',
    created_at      TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    creator         BIGINT
        COMMENT '创建人',
    UNIQUE KEY (client_id, client_secret, type),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 授权配置信息表';
