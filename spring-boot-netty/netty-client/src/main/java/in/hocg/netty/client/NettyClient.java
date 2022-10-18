package in.hocg.netty.client;

import in.hocg.netty.core.body.request.TestRequest;
import in.hocg.netty.core.protocol.Splitter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by hocgin on 2019/3/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "nginx.hocgin.dev";
    private static final int PORT = 10801;


    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<Channel>() {
                @Override
                public void initChannel(Channel ch) {
                    ch.pipeline()
                        .addLast(new LoggingHandler(LogLevel.DEBUG))
                        .addLast(new Splitter())
                        .addLast(new MessageCodec())
                    ;
                }
            });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                consoleWrite(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                    .SECONDS);
            }
        });
    }

    private static void consoleWrite(Channel channel) throws InterruptedException {
        new Thread(() -> {
            for (; ; ) {
                TestRequest testRequest = new TestRequest();
                String body = "Hello World";
                testRequest.setMessage(body);
                channel.writeAndFlush(testRequest);
                System.out.println(String.format("正在发送: %s", testRequest));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
