package in.hocg.boot.distributed.lock.core;

/**
 * Created by hocgin on 2019-09-29.
 * email: hocgin@gmail.com
 * KEY 的生成方式
 *
 * @author hocgin
 */
public enum KeyType {
    // 使用注解的 key 值
    None,
    // 使用函数参数
    Parameter,
    // 使用函数名
    MethodName
}
