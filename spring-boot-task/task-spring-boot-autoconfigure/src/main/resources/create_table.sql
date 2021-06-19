DROP TABLE IF EXISTS `boot_task_info`;
CREATE TABLE `boot_task_info`
(
    id                BIGINT AUTO_INCREMENT,
    task_sn           VARCHAR(32)  NOT NULL
        COMMENT '任务编号',
    title             VARCHAR(32)  NOT NULL DEFAULT 'unknown'
        COMMENT '任务名称',
    type              VARCHAR(32)  NOT NULL
        COMMENT '任务类型',
    status            VARCHAR(32)  NOT NULL DEFAULT 'ready'
        COMMENT '任务状态: [ready=>准备完成, executing=>执行中, done=>结束]',
    params            TEXT
        COMMENT '任务参数',
    ready_at          TIMESTAMP(6)
        COMMENT '准备完成时间',
    start_at          TIMESTAMP(6)
        COMMENT '开始执行时间',
    done_at           TIMESTAMP(6)
        COMMENT '执行结束时间',
    done_status       VARCHAR(32)
        COMMENT '执行结束的符合预期状态[success=>成功;part_fail=>部分失败;fail=>失败]',
    done_message      VARCHAR(256)
        COMMENT '执行结束产生的消息',
    done_result       TEXT
        COMMENT '执行结束产生的结果',
    total_time_millis BIGINT
        COMMENT '总耗时: ms',
    # ---
    created_at        TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    creator           BIGINT
        COMMENT '创建人',
    UNIQUE KEY (task_sn),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 任务表';

DROP TABLE IF EXISTS `boot_task_log`;
CREATE TABLE `boot_task_log`
(
    id         BIGINT AUTO_INCREMENT,
    task_id    BIGINT       NOT NULL
        COMMENT '任务ID',
    content    TEXT         NOT NULL
        COMMENT '日志信息',
    created_at TIMESTAMP(6) NOT NULL
        COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '[BOOT] 任务日志表';
