package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public boolean has(SFunction<T, ?> field, Object val, SFunction<T, ?> ignoreField, Serializable... ignoreVal) {
        List<Serializable> ignoreIds = Arrays.asList(ignoreVal).parallelStream()
            .filter(Objects::nonNull).collect(Collectors.toList());
        return !lambdaQuery().eq(field, val)
            .notIn(!ignoreIds.isEmpty(), ignoreField, Arrays.stream(ignoreVal).toArray())
            .page(new Page<>(1, 1, false)).getRecords().isEmpty();
    }

}
