package in.hocg.netty.core.invoker;

import cn.hutool.core.util.ObjectUtil;
import in.hocg.netty.core.annotation.ChannelId;
import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.annotation.PacketData;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import in.hocg.netty.core.session.ForwardCenter;
import in.hocg.netty.core.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class InvokerProxy implements InvocationHandler {
    private final SessionManager.ChanelType chanelType;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 目标channel / 消息包
        Command command = method.getAnnotation(Command.class);
        Serializable channelArg = null;
        PacketData packetAnno = null;
        Object packetDataArg = null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                if (annotation instanceof ChannelId && args[i] instanceof Serializable) {
                    channelArg = (Serializable) args[i];
                }
                if (annotation instanceof PacketData) {
                    packetAnno = (PacketData) annotation;
                    packetDataArg = args[i];
                }
            }
        }

        SerializerAlgorithm algorithm = ObjectUtil.isNull(packetAnno) ? SerializerAlgorithm.JSON : packetAnno.algorithm();
        Packet packet = new Packet(command.version(), algorithm.algorithm(), command.module(), command.value(), algorithm.serialize(packetDataArg));

        // 去发送消息 ForwardCenter
        ForwardCenter.sendAsync(chanelType, channelArg, packet);
        log.info("==> 发送消息到转发器");
        return null;
    }

}
