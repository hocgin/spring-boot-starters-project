package in.hocg.boot.mybatis.plus.extensions.httplog.enums;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum Status implements ICode {
    Executing("executing", "执行中"),
    Success("success", "成功"),
    Fail("fail", "失败");
    private final Serializable code;
    private final String name;
}

