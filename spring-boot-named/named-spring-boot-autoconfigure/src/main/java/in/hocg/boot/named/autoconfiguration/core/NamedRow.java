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
    /**
     * 源对象
     */
    private Object target;
    /**
     * 源参数
     */
    private String[] args;
    /**
     * 处理类型
     */
    private String namedType;
    /**
     * 源值
     */
    private Object idValue;
    /**
     * 目标字段
     */
    private Field targetField;
    /**
     * 目标值
     */
    private Object targetValue;
    /**
     * 提供服务Bean
     */
    private Class<?> serviceClass;
    /**
     * 是否使用缓存
     */
    private Boolean useCache;
}
