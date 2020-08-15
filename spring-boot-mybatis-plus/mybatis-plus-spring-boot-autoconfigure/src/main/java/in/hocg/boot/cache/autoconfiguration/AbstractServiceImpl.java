package in.hocg.boot.cache.autoconfiguration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
    public void validInsertOrUpdate(T entity) {
        if (Objects.nonNull(entity.pkVal())) {
            validUpdateById(entity);
        } else {
            validInsert(entity);
        }
    }

}
