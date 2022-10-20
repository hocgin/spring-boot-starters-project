package in.hocg.boot.netty.client.autoconfiguration.bean;

import in.hocg.netty.core.invoker.InvokerManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

@Getter
@Setter
public class InvokerFactoryBean implements FactoryBean<Object> {
    private Class<?> type;
    @Override
    public Object getObject() throws Exception {
        return InvokerManager.createProxy(type);
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }
}
