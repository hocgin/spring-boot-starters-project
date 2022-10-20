package in.hocg.netty.core.invoker;

import in.hocg.netty.core.annotation.Command;
import in.hocg.netty.core.constant.SystemPacketConstant;
import io.netty.util.internal.PlatformDependent;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

/**
 * @author hocgin
 * @date 18-7-31
 **/
@Slf4j
@UtilityClass
public final class BeInvokerManager {
    private static final Map<String, BeInvokerMethod> INVOKER_METHODS = PlatformDependent.newConcurrentHashMap();

    /**
     * 键码生成策略
     *
     * @param module
     * @param command
     * @return
     */
    private static String ofKey(byte module, byte command) {
        return String.format("%d-%d", module, command);
    }

    /**
     * 获取执行器
     *
     * @param module
     * @param command
     * @return
     */
    public static Optional<BeInvokerMethod> getInvoker(byte module, byte command) {
        return Optional.ofNullable(INVOKER_METHODS.get(ofKey(module, command)));
    }

    public static Optional<BeInvokerMethod> getInvoker(byte command) {
        return getInvoker(SystemPacketConstant.DefaultModule, command);
    }

    public static BeInvokerMethod getThrowInvoker(byte module, byte command) {
        return getInvoker(module, command).orElseThrow();
    }

    public static BeInvokerMethod getThrowInvoker(byte command) {
        return getInvoker(command).orElseThrow();
    }

    public static void registerCommand(Command command, BeInvokerMethod invoker) {
        String key = ofKey(command.module(), command.value());
        if (BeInvokerManager.INVOKER_METHODS.containsKey(key)) {
            log.error("标识({})已经存在", key);
        } else {
            log.debug("类名: {}, 函数: {} => {}", invoker.getTarget().getClass(), invoker.getMethod().getName(), key);
            BeInvokerManager.INVOKER_METHODS.put(key, invoker);
        }
    }
}
