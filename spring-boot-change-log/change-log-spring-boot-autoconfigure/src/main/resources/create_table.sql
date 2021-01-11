DROP TABLE IF EXISTS `com_change_log`;
CREATE TABLE `com_change_log`
(
    id          BIGINT AUTO_INCREMENT,
    log_sn      VARCHAR(32)  NOT NULL UNIQUE,
    ref_type    VARCHAR(32)  NOT NULL
        COMMENT '业务类型: 如: 订单',
    ref_id      BIGINT       NOT NULL
        COMMENT '业务ID: 如: 订单ID',
    change_type VARCHAR(8)   NOT NULL
        COMMENT '操作类型: 0->新增, 1->修改, 2->删除',
    created_at  TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    creator     BIGINT
        COMMENT '创建人',
    PRIMARY KEY (id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT '[基础模块] 业务操作日志表';

DROP TABLE IF EXISTS `com_field_change`;
CREATE TABLE `com_field_change`
(
    id            BIGINT AUTO_INCREMENT,
    change_log_id BIGINT       NOT NULL
        COMMENT '业务操作日志',
    field_name    VARCHAR(32)  NOT NULL
        COMMENT '字段名',
    field_remark  VARCHAR(255) NOT NULL
        COMMENT '字段备注',
    change_remark VARCHAR(255) NOT NULL
        COMMENT '变更描述',
    before_value  VARCHAR(255) NOT NULL
        COMMENT '变更前',
    after_value   VARCHAR(255) NOT NULL
        COMMENT '变更后',

    FOREIGN KEY (`change_log_id`) REFERENCES com_change_log (`id`),
    PRIMARY KEY (id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COMMENT '[基础模块] 业务日志-字段变更记录表';
