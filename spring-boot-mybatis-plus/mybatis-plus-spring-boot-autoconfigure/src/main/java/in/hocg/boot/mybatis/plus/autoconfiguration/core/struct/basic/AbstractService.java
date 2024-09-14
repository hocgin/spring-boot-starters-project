package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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
     * 获取查询条件的第一条记录
     *
     * @param wrapper 查询条件
     * @return 结果
     */
    Optional<T> first(Wrapper<T> wrapper);

    /**
     * 获取条件的前面几条数据
     *
     * @param queryWrapper 条件
     * @param limit        条数
     * @return
     */
    List<T> limit(Wrapper<T> queryWrapper, Integer limit);

    /**
     * 是否存在某个字段等于某个值的
     *
     * @param field
     * @param fieldVal
     * @param ignoreField
     * @param ignoreVal
     * @return
     */
    boolean has(SFunction<T, ?> field, Object fieldVal, SFunction<T, ?> ignoreField, Serializable... ignoreVal);

    boolean has(SFunction<T, ?> field, Object val, SFunction<T, ?> ignoreField, List<Serializable> ignoreVals);

    List<T> listBy(SFunction<T, ?> field, List<?> values);

    /**
     * 实体转换
     *
     * @param collection 集合
     * @param clazz      转换目标
     * @param <R>        转换目标
     * @return 转换目标实体
     */
    <R> List<R> as(List<T> collection, Class<R> clazz);

    /**
     * 实体转换
     *
     * @param opt   可选实体
     * @param clazz 转换目标
     * @param <R>   转换目标
     * @return 转换目标实体
     */
    <R> Optional<R> as(Optional<T> opt, Class<R> clazz);

    /**
     * 实体转换
     *
     * @param scroll 滚动
     * @param clazz  转换目标
     * @param <R>    转换目标
     * @return 转换目标实体
     */
    <R> IScroll<R> as(IScroll<T> scroll, Class<R> clazz);

    /**
     * 实体转换
     *
     * @param page  分页
     * @param clazz 转换目标
     * @param <R>   转换目标
     * @return 转换目标实体
     */
    <R> IPage<R> as(IPage<T> page, Class<R> clazz);

    /**
     * 实体转换
     *
     * @param entity 实体
     * @param clazz  转换目标
     * @param <R>    实体
     * @return 转换目标实体
     */
    <R> R as(T entity, Class<R> clazz);

    /**
     * 实体转换
     *
     * @param entity  实体
     * @param convert 转换函数
     * @param <R>     实体
     * @return 转换目标实体
     */
    <R> R as(T entity, SFunction<T, R> convert);

    /**
     * 实体转换
     *
     * @param list    实体
     * @param convert 转换函数
     * @param <R>     实体
     * @return 转换目标实体
     */
    <R> List<R> as(List<T> list, SFunction<T, R> convert);

    /**
     * 实体转换
     *
     * @param page    实体
     * @param convert 转换函数
     * @param <R>     实体
     * @return 转换目标实体
     */
    <R> IPage<R> as(IPage<T> page, SFunction<T, R> convert);

    /**
     * 实体转换
     *
     * @param scroll  实体
     * @param convert 转换函数
     * @param <R>     实体
     * @return 转换目标实体
     */
    <R> IScroll<R> as(IScroll<T> scroll, SFunction<T, R> convert);

    /**
     * 实体转换
     *
     * @param opt     实体
     * @param convert 转换函数
     * @param <R>     实体
     * @return 转换目标实体
     */
    <R> Optional<R> as(Optional<T> opt, SFunction<T, R> convert);

    /**
     * 实体转换
     *
     * @param entity    实体
     * @param clazz     转换目标
     * @param beanClass 转换处理器
     * @param <R>       转换目标
     * @return 转换目标实体
     */
    <R> R as(T entity, Class<R> clazz, Class<?> beanClass);
}
