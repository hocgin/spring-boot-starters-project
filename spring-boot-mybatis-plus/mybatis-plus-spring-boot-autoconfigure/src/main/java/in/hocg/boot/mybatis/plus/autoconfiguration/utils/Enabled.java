package in.hocg.boot.mybatis.plus.autoconfiguration.utils;

import in.hocg.boot.mybatis.plus.autoconfiguration.constant.DataDictEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum Enabled implements DataDictEnum {
    On(1, "开启"),
    Off(0, "关闭");
    private final Serializable code;
    private final String name;
}
