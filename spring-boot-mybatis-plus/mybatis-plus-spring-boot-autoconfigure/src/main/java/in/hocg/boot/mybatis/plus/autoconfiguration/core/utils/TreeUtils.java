package in.hocg.boot.mybatis.plus.autoconfiguration.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hocgin on 2022/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TreeUtils {
    public static final String PATH_SEPARATOR = "/";

    public List<Long> toPath(String path) {
        return Stream.of(StrUtil.split(path.replaceFirst(PATH_SEPARATOR, StrUtil.EMPTY), PATH_SEPARATOR))
            .map(Convert::toLong).collect(Collectors.toList());
    }

    public String toPath(Long... path) {
        return PATH_SEPARATOR + StrUtil.join(PATH_SEPARATOR, path);
    }
}
