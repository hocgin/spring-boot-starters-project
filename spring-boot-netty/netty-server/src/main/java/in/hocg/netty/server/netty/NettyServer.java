package in.hocg.netty.server.netty;

/**
 * Created by hocgin on 2019/3/2.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface NettyServer {
    /**
     * 启动
     */
    void start();

    /**
     * 摧毁
     */
    void destroy();
}
