package in.hocg.netty.core.invoker;

import in.hocg.netty.core.annotation.ChannelId;
import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.annotation.PacketData;
import in.hocg.netty.core.protocol.packet.Packet;
import in.hocg.netty.core.serializer.SerializerAlgorithm;
import in.hocg.netty.core.session.ForwardCenter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class InvokerProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 目标channel / 消息包
        Command command = method.getAnnotation(Command.class);
        Serializable channelArg = null;
        Object packetDataArg = null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                if (annotation instanceof ChannelId && args[i] instanceof Serializable) {
                    channelArg = (Serializable) args[i];
                }
                if (annotation instanceof PacketData) {
                    packetDataArg = args[i];
                }
            }
        }

        SerializerAlgorithm json = SerializerAlgorithm.JSON;
        Packet packet = new Packet(command.version(), json.algorithm(), command.module(), command.value(), json.serialize(packetDataArg));

        // 去发送消息 ForwardCenter
        ForwardCenter.sendAsync(channelArg, packet);
        log.info("==> 发送消息到转发器");
        return null;
    }

}
