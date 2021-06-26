package in.hocg.boot.vars.autoconfiguration.core;


import java.util.Optional;

/**
 * Created by hocgin on 2021/6/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface VarsConfigRepository {

    /**
     * 设置键值
     *
     * @param key   键
     * @param value 值
     */
    default void set(String key, Object value) {
        this.set(key, value, null, null);
    }

    /**
     * 设置键值
     *
     * @param key    键
     * @param value  值
     * @param title  标题
     * @param remark 描述
     */
    void set(String key, Object value, String title, String remark);

    /**
     * 获取键值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Optional<T> getValue(String key, Class<T> clazz);

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    String getValue(String key);
}
