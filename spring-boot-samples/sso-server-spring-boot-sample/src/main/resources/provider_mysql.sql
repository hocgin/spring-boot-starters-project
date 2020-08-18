-- used in tests that use HSQL
create table oauth_client_details
(
    client_id               VARCHAR(256) PRIMARY KEY
        COMMENT '客户端ID',
    resource_ids            VARCHAR(256)
        COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
    client_secret           VARCHAR(256)
        COMMENT '客户端密匙',
    scope                   VARCHAR(256)
        COMMENT '客户端申请的权限范围',
    authorized_grant_types  VARCHAR(256)
        COMMENT '客户端支持的grant_type',
    web_server_redirect_uri VARCHAR(256)
        COMMENT '重定向URI',
    authorities             VARCHAR(256)
        COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
    access_token_validity   INTEGER
        COMMENT '访问令牌有效时间值(单位:秒)',
    refresh_token_validity  INTEGER
        COMMENT '更新令牌有效时间值(单位:秒)',
    additional_information  VARCHAR(4096)
        COMMENT '预留字段',
    autoapprove             VARCHAR(256)
        COMMENT '用户是否自动Approval操作'
);

create table oauth_access_token
(
    token_id          VARCHAR(256)
        COMMENT 'MD5加密的access_token的值',
    token             LONGBLOB
        COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
    authentication_id VARCHAR(256) PRIMARY KEY
        COMMENT 'MD5加密过的username,client_id,scope',
    user_name         VARCHAR(256)
        COMMENT '登录的用户名',
    client_id         VARCHAR(256)
        COMMENT '客户端ID',
    authentication    LONGBLOB
        COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
    refresh_token     VARCHAR(256)
        COMMENT 'MD5加密果的refresh_token的值'
);

create table oauth_refresh_token
(
    token_id       VARCHAR(256)
        COMMENT 'MD5加密过的refresh_token的值',
    token          LONGBLOB
        COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据',
    authentication LONGBLOB
        COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据'
);

create table oauth_code
(
    code           VARCHAR(256)
        COMMENT '授权码(未加密)',
    authentication LONGBLOB
        COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据'
);

create table oauth_approvals
(
    userId         VARCHAR(256)
        COMMENT '登录的用户名',
    clientId       VARCHAR(256)
        COMMENT '客户端ID',
    scope          VARCHAR(256)
        COMMENT '申请的权限',
    status         VARCHAR(10)
        COMMENT '状态（Approve或Deny）',
    expiresAt      TIMESTAMP
        COMMENT '过期时间',
    lastModifiedAt TIMESTAMP
        COMMENT '最终修改时间'
);
