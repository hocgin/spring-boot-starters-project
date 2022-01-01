package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/2/11.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface AbstractService<T> extends IService<T> {

    /**
     * 校验数据的约束
     *
     * @param entity
     */
    void validEntity(T entity);

    /**
     * 校验后，更新数据
     *
     * @param entity
     * @return
     */
    boolean validUpdateById(T entity);

    /**
     * 校验后，新增数据
     *
     * @param entity
     * @return
     */
    boolean validInsert(T entity);

    /**
     * 校验后，新增或更新数据
     *
     * @param entity
     */
    boolean validInsertOrUpdate(T entity);

    /**
     * 是否存在某个字段等于某个值的
     *
     * @param field
     * @param fieldVal
     * @param ignoreField
     * @param ignoreVal
     * @return
     */
    boolean has(SFunction<T, ?> field, Object fieldVal,
                SFunction<T, ?> ignoreField, Serializable... ignoreVal);
}
