package in.hocg.boot.web.sample.listeners;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.EntityListener;
import in.hocg.boot.web.sample.data.basic.ModelEntity;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class TestEntityListener implements EntityListener<ModelEntity> {

    public void onPreInsert(ModelEntity entity) {
        System.out.println("PreInsert");
    }

    public void onPreUpdate(ModelEntity entity) {
        System.out.println("PreUpdate");
    }
}
