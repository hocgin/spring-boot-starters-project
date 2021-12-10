package in.hocg.named.sample;

import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.named.sample.alias.AppNamed;
import in.hocg.named.sample.basic.AppNamed2Service;
import in.hocg.named.sample.basic.AppNamedService;
import in.hocg.named.sample.basic.NamedConstants;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

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
    private Serializable code;
    @Named(idFor = "code", type = NamedConstants.Test, useService = AppNamedService.class)
    private String code1Name;
    @Named(idFor = "code", type = NamedConstants.Test2, useService = AppNamed2Service.class)
    private String code2Name;
    @Named(idFor = "code", type = NamedConstants.Test3)
    private String code3Name;
    @AppNamed(idFor = "code", type = NamedConstants.Test)
    private String code4Name;
    private List<TestBean> list = Collections.emptyList();
}
