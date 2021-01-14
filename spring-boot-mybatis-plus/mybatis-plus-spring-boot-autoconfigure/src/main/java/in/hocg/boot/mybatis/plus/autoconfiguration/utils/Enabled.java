package in.hocg.boot.mybatis.plus.autoconfiguration.utils;

import in.hocg.boot.utils.enums.DataDictEnum;
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
    On("on", "开启"),
    Off("off", "关闭");
    private final Serializable code;
    private final String name;
    public final static String KEY = "enabled";
}
