package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners;

/**
 * Created by hocgin on 2022/1/3
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface EntityListener<T> {
    /**
     * 新增之前触发
     *
     * @param entity 实体
     */
    default void onPreInsert(T entity) {
    }

    /**
     * 更新之前触发
     *
     * @param entity 实体
     */
    default void onPreUpdate(T entity) {
    }
}
