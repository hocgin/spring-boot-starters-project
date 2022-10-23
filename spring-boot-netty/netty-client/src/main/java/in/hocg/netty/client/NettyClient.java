package in.hocg.netty.client;

import cn.hutool.core.date.DateUtil;
import in.hocg.netty.core.protocol.Splitter;
import in.hocg.netty.core.protocol.codec.MessageCodec;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.session.SessionManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created by hocgin on 2019/3/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class NettyClient {
    private static final int MAX_RETRY = 15;
    private final Bootstrap bootstrap;
    private final Consumer<Channel> onSuccess;
    private String host;
    private int port;
    private Serializable channelId;

    public synchronized NettyClient bindChannel(Channel channel) {
        Serializable channelId = channel.id().asLongText();
        SessionManager.add(SessionManager.ChannelType.Client, channelId, channel);
        log.debug("Remote Channel.id={}", channelId);
        this.channelId = channelId;
        return this;
    }

    public synchronized NettyClient unbindChannel() {
        if (Objects.nonNull(this.channelId)) {
            SessionManager.remove(SessionManager.ChannelType.Client, this.channelId);
            this.channelId = null;
        }
        return this;
    }

    public synchronized Optional<Serializable> getChannelId() {
        return Optional.ofNullable(this.channelId);
    }

    public Serializable getChannelIdThrow() {
        return getChannelId().orElseThrow();
    }

    public synchronized Optional<Channel> getChannel() {
        return getChannelId().map(channelId -> SessionManager.get(SessionManager.ChannelType.Client, channelId));
    }

    private NettyClient(Bootstrap bootstrap, Consumer<Channel> onSuccess) {
        this.bootstrap = bootstrap;
        this.onSuccess = onSuccess;
    }

    public static NettyClient create() {
        return create((channel) -> {
        });
    }

    public static NettyClient create(Consumer<Channel> onSuccess) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        NettyClient client = new NettyClient(bootstrap, onSuccess);
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<>() {
                @Override
                public void initChannel(Channel ch) {
                    ch.pipeline()
                        .addLast(new LoggingHandler(LogLevel.DEBUG))
                        .addLast(new Splitter())
                        .addLast(new MessageCodec())
                        .addLast(new ServerChannelClosedHandler(client))
                    ;
                }
            });
        return client;
    }


    public ChannelFuture start(String host, int port) {
        return start(host, port, -1);
    }

    public ChannelFuture start(int retry) {
        return start(this.host, this.port, retry);
    }

    public ChannelFuture start(String host, int port, int retry) {
        this.host = host;
        this.port = port;
        return bootstrap.connect(host, port).addListener(future -> {
            if (future.isCancellable()) {
                log.warn("连接中断了");
            }
            if (future.isSuccess()) {
                Channel channel = ((ChannelFuture) future).channel();
                this.bindChannel(channel);
                log.info(new Date() + ": 连接成功，启动控制台线程……");
                if (Objects.nonNull(onSuccess)) {
                    this.onSuccess.accept(channel);
                }
            } else if (retry == 0) {
                log.warn("重试次数已用完，放弃连接！");
            } else {
                this.reconnect(retry);
            }
        });
    }

    public void reconnect() {
        reconnect(-1);
    }

    public void reconnect(int retry) {
        int delay = 5;
        // 第几次重连
        int order = -1;
        if (retry > 0) {
            order = (MAX_RETRY - retry) + 1;
            delay = 1 << order;
        }
        log.warn("{}: 连接失败，第{}次重连……", DateUtil.now(), order);
        bootstrap.config().group().schedule(() -> start(this.host, this.port, retry - 1), delay, TimeUnit.SECONDS);
    }

    public NettyClient sendPacket(Packet packet) {
        Channel channel = getChannel().orElseThrow();
        channel.writeAndFlush(packet);
        return this;
    }

}
