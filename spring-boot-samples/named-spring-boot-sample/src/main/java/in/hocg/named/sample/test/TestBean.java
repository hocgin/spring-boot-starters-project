package in.hocg.named.sample.test;

import in.hocg.boot.named.spring.boot.autoconfiguration.annotation.InjectNamed;
import in.hocg.boot.named.spring.boot.autoconfiguration.annotation.Named;
import in.hocg.named.sample.named.NamedConstants;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@InjectNamed
public class TestBean {
    private String code;
    @Named(idFor = "code", type = NamedConstants.Test)
    private String codeName;
}
