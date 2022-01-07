package in.hocg.boot.mybatis.plus.sample.convert;

import in.hocg.boot.mybatis.plus.sample.dto.ExampleVo;
import in.hocg.boot.mybatis.plus.sample.entity.Example;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class ExampleConvert {

    public ExampleVo asExampleVo(Example entity) {
        return new ExampleVo().setOk("ok");
    }
}
