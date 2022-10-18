package in.hocg.boot.netty.server.autoconfiguration.bean;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hocgin
 * @date 18-7-31
 **/
@Data
public class Invoker {
    private Class<?> targetClass;
    private Method method;

    public static Invoker valueOf(Method method, Class<?> target){
        Invoker invoker = new Invoker();
        invoker.setTargetClass(target);
        invoker.setMethod(method);
        return invoker;
    }

    public Object invoke(Object... paramValues){
        try {
            return method.invoke(SpringContext.getBean(targetClass), paramValues);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
