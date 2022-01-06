package in.hocg.boot.ws.autoconfiguration.core.service.table;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TableService {

    /**
     * 创建会话
     *
     * @param sessionId 会话ID
     */
    void create(String sessionId);

    /**
     * 登录(绑定会话)
     *
     * @param sessionId 会话ID
     * @param userKey   用户标识
     */
    void login(String sessionId, String userKey);

    /**
     * 下线用户
     *
     * @param userKey 用户唯一标识
     */
    void logout(String userKey);

    /**
     * 下线会话
     *
     * @param sessionId 会话
     */
    void remove(String sessionId);

    /**
     * 心跳
     *
     * @param sessionId 会话
     */
    void heartbeat(String sessionId);
}
