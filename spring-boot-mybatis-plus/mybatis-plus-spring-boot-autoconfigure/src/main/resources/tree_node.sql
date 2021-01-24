create table t_tree_node
(
    `id`        BIGINT AUTO_INCREMENT,
    --
    `parent_id` BIGINT
        COMMENT '父级ID, 顶级为 NULL',
    `tree_path` varchar(255) NOT NULL UNIQUE
        COMMENT '树路径，组成方式: /父路径/当前ID',
    `enabled`   TINYINT(1) UNSIGNED NOT NULL DEFAULT 1
        COMMENT '启用状态',
);
