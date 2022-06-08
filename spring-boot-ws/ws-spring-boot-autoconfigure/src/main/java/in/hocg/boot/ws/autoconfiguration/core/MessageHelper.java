package in.hocg.boot.ws.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import in.hocg.boot.ws.autoconfiguration.core.constant.StringConstants;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2022/1/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class MessageHelper {
    public static final String PREFIX_BROADCAST = "/queue";
    public static final String PREFIX_USER = PREFIX_BROADCAST;

    /**
     * 广播
     *
     * @param path
     * @return
     */
    public String toBroadcast(String... paths) {
        String path = Joiner.on(StringConstants.PATH_SEPARATOR).skipNulls().join(paths);
        return StrUtil.format("{}{}", MessageHelper.PREFIX_BROADCAST, path);
    }

    /**
     * 点对点
     *
     * @param path
     * @return
     */
    public String toUser(String... paths) {
        String path = Joiner.on(StringConstants.PATH_SEPARATOR).skipNulls().join(paths);
        return StrUtil.format("{}{}", MessageHelper.PREFIX_USER, path);
    }
}
