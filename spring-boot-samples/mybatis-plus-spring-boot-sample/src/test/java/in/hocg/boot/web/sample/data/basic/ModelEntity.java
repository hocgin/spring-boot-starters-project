package in.hocg.boot.web.sample.data.basic;

import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.EntityListeners;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.LogicDeletedEntity;
import in.hocg.boot.web.sample.listeners.TestEntityListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("model_entity")
@EntityListeners(TestEntityListener.class)
public class ModelEntity extends LogicDeletedEntity<ModelEntity> {
    private String title;
}
