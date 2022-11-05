Java SDK使用说明

环境依赖

- Java SE/EE 1.7及以上（不支持Android平台）

使用示例

获取access_token

    PopAccessTokenClient client = new PopAccessTokenClient(
                    clientId,
                    clientSecret);
    AccessTokenResponse accessTokenResponse = client.generate(code);
             

刷新access_token

    PopAccessTokenClient client = new PopAccessTokenClient(
                    clientId,
                    clientSecret);
    AccessTokenResponse accessTokenResponse = client.refresh(refreshToken);

API调用（以pdd.order.list.get为例）

    PopClient client = new PopHttpClient(
                    clientId,
                    clientSecret);
    PddOrderListGetRequest request = new PddOrderListGetRequest();
    request.setRefundStatus(refundStatus);
    request.setOrderStatus(orderStatus);
    request.setStartConfirmAt(startConfirmAt);
    request.setEndConfirmAt(endConfirmAt);
    request.setPage(page);
    request.setPageSize(pageSize);
    try {
    	//同步调用
        PddOrderListGetResponse response = client.syncInvoke(request,accessToken);
        //异步调用
        Future<PddOrderListGetResponse> futureResponse = client.asyncInvoke(request,accessToken);
    } catch (Exception e){
        System.out.println(e);
    }

消息客户端

    WsClient ws = new WsClient(
            clientId,
            clientSecret,
            new MessageHandler() {
        		public void onMessage(Message message) throws InterruptedException {
    	        	System.out.println("receive message: " + message);
        		}
        	});
    ws.connect();

服务地址

access_token获取地址

https://open-api.pinduoduo.com/oauth/token

API服务地址

https://gw-api.pinduoduo.com/api/router

消息服务地址

wss://message-api.pinduoduo.com



SDK中已经默认设置了以上地址。

其他功能

用户可以通过修改默认的HttpClientConfig自定义Http客户端配置，默认配置如下：

    // 连接配置
    private int connectionTimeoutMillis = 5000;
    private int socketTimeoutMillis = 5000
    private int connectionRequestTimeout = 1000;
    
    // 异步请求线程池配置
    private int maxParallel = 10;
    private long threadKeepAliveTime = 30;
    
    // connectionManager配置
    private int maxTotal = 50;
    private int defaultMaxPerRoute = 20;

注意事项

- PopHttpClient是线程安全的，所以没有必要每次API请求都新建一个PopHttpClient
- WsClient支持心跳重连，心跳发送间隔为10秒钟，心跳超时时间为3分钟


