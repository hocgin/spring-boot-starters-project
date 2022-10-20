package in.hocg.boot.netty.sample;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.netty.client.NettyClient;
import in.hocg.netty.core.constant.GlobalConstant;
import in.hocg.netty.core.constant.SystemPacket;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class NettyClientTests {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = GlobalConstant.DEFAULT_PORT;
    private NettyClient nettyClient;

    @Test
    public void testNetty() throws InterruptedException {
        nettyClient = NettyClient.create(this::onSuccess);
        nettyClient.start(HOST, PORT);
        Thread.sleep(10000000);
    }

    public void onSuccess(Channel channel) {
        for (; ; ) {
            if (!channel.isActive()) {
                log.warn("失去连接");
                break;
            }
            PacketRo packetData = new PacketRo().setTitle(StrUtil.format("当前时间.{}", DateUtil.now()));
            // ====
            SerializerAlgorithm algorithm = SerializerAlgorithm.JSON;
            Packet packet = new Packet(SystemPacket.Version, algorithm.algorithm(),
                TestModule.MODULE_VALUE, TestModule.TEST_REQUEST_2, algorithm.serialize(packetData));
            nettyClient.sendPacket(packet);
            log.info("正在发送: {}", packet);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
