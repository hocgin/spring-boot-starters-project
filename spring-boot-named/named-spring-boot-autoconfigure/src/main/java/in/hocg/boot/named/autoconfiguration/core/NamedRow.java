package in.hocg.boot.named.autoconfiguration.core;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;

/**
 * Created by hocgin on 2020/8/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class NamedRow {
    private Object target;
    private String[] args;
    private String namedType;
    private Object idValue;
    private Field targetField;
    private Object targetValue;
    private Class<?> serviceClass;
}
