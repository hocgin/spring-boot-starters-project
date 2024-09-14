package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.ChainQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.convert.UseConvert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.utils.LangUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class AbstractServiceImpl<M extends BaseMapper<T>, T extends AbstractEntity<?>> extends ServiceImpl<M, T>
    implements AbstractService<T> {

    @Override
    public void validEntity(T entity) {
        // Default SUCCESS
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validUpdateById(T entity) {
        validEntity(entity);
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validInsert(T entity) {
        validEntity(entity);
        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validInsertOrUpdate(T entity) {
        boolean isOk;
        if (Objects.nonNull(entity.pkVal())) {
            isOk = validUpdateById(entity);
        } else {
            isOk = validInsert(entity);
        }
        return isOk;
    }

    @Override
    public Optional<T> first(Wrapper<T> queryWrapper) {
        List<T> records = limit(queryWrapper, 1);
        if (records.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(records.get(0));
    }

    @Override
    public List<T> limit(Wrapper<T> queryWrapper, Integer limit) {
        Page<T> limitPage = new Page<>(1, limit, false);

        if (queryWrapper instanceof ChainQuery) {
            return ((ChainQuery<T>) queryWrapper).page(limitPage).getRecords();
        }
        return page(limitPage, queryWrapper).getRecords();
    }

    @Override
    public boolean has(SFunction<T, ?> field, Object val, SFunction<T, ?> ignoreField, Serializable... ignoreVal) {
        List<Serializable> ignoreIds = Arrays.asList(ignoreVal).parallelStream()
            .filter(Objects::nonNull).collect(Collectors.toList());
        return this.has(field, val, ignoreField, ignoreIds);
    }

    @Override
    public boolean has(SFunction<T, ?> field, Object val, SFunction<T, ?> ignoreField, List<Serializable> ignoreVals) {
        if (CollUtil.isEmpty(ignoreVals)) {
            ignoreVals = Collections.emptyList();
        } else {
            ignoreVals = ignoreVals.stream().filter(Objects::nonNull).collect(Collectors.toList());
        }
        return !lambdaQuery().eq(field, val)
            .notIn(!ignoreVals.isEmpty(), ignoreField, ignoreVals.toArray())
            .page(new Page<>(1, 1, false)).getRecords().isEmpty();
    }

    @Override
    public List<T> listBy(SFunction<T, ?> field, List<?> values) {
        if (CollUtil.isEmpty(values)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(field, values).list();
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public <R> List<R> as(List<T> collection, Class<R> clazz) {
        return LangUtils.toList(collection, item -> as(item, clazz));
    }

    @Override
    public <R> Optional<R> as(Optional<T> opt, Class<R> clazz) {
        return opt.map(item -> as(item, clazz));
    }

    @Override
    public <R> IScroll<R> as(IScroll<T> scroll, Class<R> clazz) {
        return scroll.convert(item -> as(item, clazz));
    }

    @Override
    public <R> IPage<R> as(IPage<T> page, Class<R> clazz) {
        return page.convert(item -> as(item, clazz));
    }

    @Override
    public <R> R as(T entity, Class<R> clazz) {
        UseConvert useConvert = getClass().getAnnotation(UseConvert.class);
        return as(entity, clazz, useConvert.value());
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override
    public <R> R as(T entity, SFunction<T, R> convert) {
        return convert.apply(entity);
    }

    @Override
    public <R> List<R> as(List<T> list, SFunction<T, R> convert) {
        return LangUtils.toList(list, convert);
    }

    @Override
    public <R> IPage<R> as(IPage<T> page, SFunction<T, R> convert) {
        return page.convert(convert);
    }

    @Override
    public <R> IScroll<R> as(IScroll<T> scroll, SFunction<T, R> convert) {
        return scroll.convert(convert);
    }

    @Override
    public <R> Optional<R> as(Optional<T> opt, SFunction<T, R> convert) {
        return opt.map(convert);
    }

    @Override
    public <R> R as(T entity, Class<R> clazz, Class<?> beanClass) {
        if (clazz.isInstance(entity)) {
            return (R) entity;
        }
        List<Method> methods = ReflectUtil.getPublicMethods(beanClass, method ->
            method.getReturnType().isAssignableFrom(clazz) && method.getParameterCount() == 1);
        if (methods.size() == 0) {
            throw new IllegalArgumentException("没有找到合适的方法");
        }
        Method method = methods.get(0);
        Object bean = SpringUtil.getBean(beanClass);
        return as(entity, (T t) -> ReflectUtil.invoke(bean, method, t));
    }
}
