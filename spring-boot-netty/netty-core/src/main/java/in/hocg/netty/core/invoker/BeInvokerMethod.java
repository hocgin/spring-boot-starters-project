package in.hocg.netty.core.invoker;

import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hocgin
 * @date 18-7-31
 **/
@Data
public class BeInvokerMethod {
    private Object target;
    private Method method;

    public static BeInvokerMethod of(Object target, Method method) {
        BeInvokerMethod invoker = new BeInvokerMethod();
        invoker.setTarget(target);
        invoker.setMethod(method);
        return invoker;
    }

    public Object invoke(Object... paramValues) {
        try {
            return method.invoke(this.target, paramValues);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
