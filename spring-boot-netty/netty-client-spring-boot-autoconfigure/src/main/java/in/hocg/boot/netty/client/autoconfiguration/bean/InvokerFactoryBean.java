package in.hocg.boot.netty.client.autoconfiguration.bean;

import in.hocg.netty.core.invoker.InvokerProxy;
import in.hocg.netty.core.session.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

@Getter
@Setter
public class InvokerFactoryBean implements FactoryBean<Object> {
    private Class<?> type;
    private SessionManager.ChannelType channelType;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(InvokerFactoryBean.class.getClassLoader(), new Class[]{type}, new InvokerProxy(channelType));
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }
}
