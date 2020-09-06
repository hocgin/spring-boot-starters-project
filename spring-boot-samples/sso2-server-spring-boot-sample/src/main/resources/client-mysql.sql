create table oauth_client_token
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
        COMMENT '客户端ID'
);
