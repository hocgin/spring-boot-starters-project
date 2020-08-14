package in.hocg.boot.mybatis.plus.autoconfiguration.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum Enabled {
    On(1),
    Off(0);
    private final Integer code;
}
