package in.hocg.named.sample.pojo;

import in.hocg.boot.named.annotation.InjectNamed;
import in.hocg.boot.named.annotation.Named;
import in.hocg.named.sample.named.CustomNamed;
import in.hocg.named.sample.named.DefaultNamedService;
import in.hocg.named.sample.named.CustomNamedService;
import in.hocg.named.sample.named.NamedConstants;
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
public class TestBeanDto {
    private Serializable code;
    @Named(idFor = "code", type = NamedConstants.Test, useService = CustomNamedService.class)
    private String code1Name;
    @Named(idFor = "code", type = NamedConstants.Test2, useService = DefaultNamedService.class)
    private String code2Name;
    @Named(idFor = "code", type = NamedConstants.ThrowTest3)
    private String code3Name;
    @CustomNamed(idFor = "code", type = NamedConstants.Test)
    private String code4Name;
    @Named(idFor = "code", type = NamedConstants.Test)
    private String code5Name;
    private List<TestBeanDto> list = Collections.emptyList();
}
