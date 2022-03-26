package in.hocg.boot.utils;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.exception.ToolsException;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/1/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ExceptionUtils {

    /**
     * 返回一个新的异常，统一构建，方便统一处理
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static ToolsException wrap(String msg, Throwable t, Object... params) {
        return new ToolsException(StrUtil.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static ToolsException wrap(String msg, Object... params) {
        return new ToolsException(StrUtil.format(msg, params));
    }

    /**
     * 重载的方法
     *
     * @param t 异常
     * @return 返回异常
     */
    public static ToolsException wrap(Throwable t) {
        return new ToolsException(t);
    }
}
