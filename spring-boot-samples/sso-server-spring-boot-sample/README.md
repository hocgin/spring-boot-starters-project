- /oauth/authorize：申请授权码 code, 涉及的类AuthorizationEndpoint
- /oauth/token：获取令牌 token, 涉及的类TokenEndpoint
- /oauth/check_token：用于资源服务器请求端点来检查令牌是否有效, 涉及的类CheckTokenEndpoint
- /oauth/confirm_access：用户确认授权提交, 涉及的类WhitelabelApprovalEndpoint
- /oauth/error：授权服务错误信息, 涉及的类WhitelabelErrorEndpoint
- /oauth/token_key：提供公有密匙的端点，使用 JWT 令牌时会使用 , 涉及的类TokenKeyEndpoint

⚠️: 默认情况下/oauth/check_token和/oauth/token_key端点默认是denyAll()
