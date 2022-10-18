package in.hocg.netty.server.netty.initializer;

import in.hocg.netty.core.protocol.IdleStateCheck;
import in.hocg.netty.server.netty.handler.AbsForwardHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;


/**
 * Created by hocgin on 2019/3/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {
    private final AbsForwardHandler forwardHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline()
            // 心跳检测
            .addLast(new IdleStateCheck())
            // HTTP协议,编解码
            .addLast("http-codec", new HttpServerCodec())
            /*
            HTTP 消息合并
              - 当消息超过 65536 会发生异常,可以对消息进行分包或增大参数容量
             */
            .addLast("aggregator", new HttpObjectAggregator(65536))
            // 分块处理
            .addLast("http-chunk", new ChunkedWriteHandler())
            // Socket 协议处理
            .addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"))
            // 业务处理器
            .addLast("HANDLER", forwardHandler)
        ;
    }
}
