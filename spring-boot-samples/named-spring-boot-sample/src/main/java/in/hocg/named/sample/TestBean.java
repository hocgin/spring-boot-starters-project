package in.hocg.named.sample;

import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.named.sample.basic.NamedConstants;
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
    @Named(idFor = "code", type = NamedConstants.Test2)
    private String code2Name;
    @Named(idFor = "code", type = NamedConstants.Test3)
    private String code3Name;
}
