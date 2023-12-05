package in.hocg.boot.test.sample.basic;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created by hocgin on 2023/12/05
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TestFactoryBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return new TestMapperProxyObject();
    }

    @Override
    public Class<?> getObjectType() {
        return TestMapperProxyObject.class;
    }
}
