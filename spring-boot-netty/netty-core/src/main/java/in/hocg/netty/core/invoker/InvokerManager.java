package in.hocg.netty.core.invoker;

import io.netty.util.internal.PlatformDependent;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;

/**
 * @author hocgin
 * @date 18-7-31
 **/
@UtilityClass
public final class InvokerManager {
    public final static String DEFAULT_MODULE = "default";
    public static final Map<String, Invoker> METHODS = PlatformDependent.newConcurrentHashMap();

    /**
     * 键码生成策略
     *
     * @param module
     * @param command
     * @return
     */
    public static String ofKey(String module, String command) {
        return String.format("%s-%s", module, command);
    }


    /**
     * 获取执行器
     *
     * @param module
     * @param command
     * @return
     */
    public static Optional<Invoker> getInvoker(String module, String command) {
        return Optional.ofNullable(METHODS.get(ofKey(module, command)));
    }

    public static Optional<Invoker> getInvoker(String command) {
        return getInvoker(DEFAULT_MODULE, command);
    }

    public static Invoker getThrowInvoker(String module, String command) {
        return getInvoker(module, command).orElseThrow();
    }

    public static Invoker getThrowInvoker(String command) {
        return getInvoker(command).orElseThrow();
    }

}
