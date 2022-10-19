package in.hocg.netty.server.netty.session;

import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hocgin on 2019/3/6.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class SessionManager {
    private static final Map<Serializable, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 登陆在线
     *
     * @param key
     * @param channel
     */
    public static Channel add(Serializable key, Channel channel) {
        return CHANNEL_MAP.put(key, channel);
    }

    /**
     * 移除在线
     *
     * @param key
     */
    public static Channel remove(Serializable key) {
        return CHANNEL_MAP.remove(key);
    }

    /**
     * 获取 Channel
     *
     * @param key
     * @return
     */
    public static Channel get(Serializable key) {
        return CHANNEL_MAP.get(key);
    }
}
