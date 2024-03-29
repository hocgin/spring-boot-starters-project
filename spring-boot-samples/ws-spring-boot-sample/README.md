### 使用说明

```shell
# WebSocket 地址: 
匿名: ws://127.0.0.1:8080/.socket
登陆: ws://127.0.0.1:8080/.socket?ticket=666

# 广播前缀，即客户端要订阅的地址
服务端发送: @SendTo("/queue/all")
客户端订阅: client.subscribe("/queue/all", handler)

# 用户点对点前缀, 即客户端要订阅的地址
服务端发送: @SendToUser(destinations = "/queue/errors") 
客户端订阅: client.subscribe('/user/queue/errors', handler);

# 异常处理, 即客户端要订阅的地址
服务端处理: WebSocketExceptionAdvice
客户端订阅地址: client.subscribe('/user/queue/errors', handler);

# 接收应用消息前缀，即客户端要发送到的目标地址
服务端订阅: @MessageMapping("/index")
客户端发送: client.send('/app/index', {});
```

### WS 支持的注解

```shell
@MessageMapping 消息路由
@SendTo         转发到广播频道
@SendToUser
@Payload        接收消息体
@Header
@Headers
@DestinationVariable
@SubscribeMapping
@MessageExceptionHandler


Principal
Message
MessageHeaderAccessor
SimpMessagingTemplate
SimpUserRegistry
```

### STOMP 协议

```shell
COMMAND
header1:value1
header2:value2

Body^@
```

### 例子

```shell
ws://127.0.0.1:8080/.socket
# http://www.easyswoole.com/wstool.html
SEND
destination:/app/index
content-type:application/json

{"test":"BUY","ok":"MMM","shares":44} 

#
SUBSCRIBE
id:sub-2
destination:/user/queue/errors

 

# 
SUBSCRIBE
id:sub-1
destination:/queue/errors

 

```

* 在线测试:
* http://www.easyswoole.com/wstool.html
* 鉴权方案:
* https://docs.spring.io/spring-security/site/docs/5.2.x/reference/html/integrations.html#websocket
* http://www.moye.me/2017/02/10/websocket-authentication-and-authorization/
* https://www.tony-bro.com/posts/3568303861/index.html
* 开发方案:
* https://www.docs4dev.com/docs/zh/spring-framework/4.3.21.RELEASE/reference/websocket.html
* https://spring.io/guides/gs/messaging-stomp-websocket/
* https://www.cnblogs.com/dream-flying/articles/13019597.html
* https://docs.spring.io/spring-framework/docs/4.3.x/spring-framework-reference/html/websocket.html
* https://blog.csdn.net/weixin_33725270/article/details/88067111
* https://blog.csdn.net/hry2015/article/details/79829616
* <p>
* 集群方案:
* https://mp.weixin.qq.com/s/QeWb-9-j5EYeB7I37gZ50A
