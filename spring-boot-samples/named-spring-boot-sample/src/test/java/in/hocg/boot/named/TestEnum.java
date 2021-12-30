package in.hocg.boot.named;

import in.hocg.boot.utils.annotation.UseDataDictKey;
import in.hocg.boot.utils.enums.DataDictEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/12/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@UseDataDictKey(value = "data_dict_key", description = "测试的数据字典")
@RequiredArgsConstructor
public enum TestEnum implements DataDictEnum {
    TEST_1("t1", "测试1"),
    TEST_2("t2", "测试1"),
    ;
    private final Serializable code;
    private final String name;
}
