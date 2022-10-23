package in.hocg.boot.netty.sample;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.netty.sample.constant.NettyConstant;
import in.hocg.boot.netty.sample.invoker.TestInvoker;
import in.hocg.boot.netty.sample.pojo.PacketRo;
import in.hocg.netty.client.NettyClient;
import in.hocg.netty.core.constant.GlobalConstant;
import in.hocg.netty.core.constant.SystemPacket;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NettyClientInBootTests {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = GlobalConstant.DEFAULT_PORT;
    NettyClient nettyClient;
    @Autowired(required = false)
    TestInvoker testInvoker;

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

            SerializerAlgorithm algorithm = SerializerAlgorithm.JSON;
            Packet packet = new Packet(SystemPacket.Version, algorithm.algorithm(),
                NettyConstant.MOD_TEST, NettyConstant.CMD_PING, algorithm.serialize(packetData));

            // 方法一: 发送数据包
            packetData.setRemark("sendPacket");
            nettyClient.sendPacket(packet);

            // 方法二: 发送数据包
            packetData.setRemark("ping");
            testInvoker.ping(nettyClient.getChannelIdThrow(), packetData);
            log.info("正在发送: {}", packet);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
