package in.hocg.netty.core.invoker;

import java.lang.reflect.Proxy;

public class InvokerManager {

    public static Object createProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(InvokerManager.class.getClassLoader(), new Class[]{clazz}, new InvokerProxy());
    }

}
