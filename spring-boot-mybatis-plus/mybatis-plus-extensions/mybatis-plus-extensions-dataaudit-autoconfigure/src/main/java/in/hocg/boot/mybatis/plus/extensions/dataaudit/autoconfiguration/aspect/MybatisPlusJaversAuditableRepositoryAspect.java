package in.hocg.boot.mybatis.plus.extensions.dataaudit.autoconfiguration.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.javers.core.Javers;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.CommitPropertiesProvider;
import org.springframework.core.annotation.Order;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Aspect
@Order(0)
public class MybatisPlusJaversAuditableRepositoryAspect extends AbstractSpringAuditableMapperAspect {
    public MybatisPlusJaversAuditableRepositoryAspect(Javers javers, AuthorProvider authorProvider, CommitPropertiesProvider commitPropertiesProvider) {
        super(javers, authorProvider, commitPropertiesProvider);
    }

    @AfterReturning("execution(public * deleteById(..)) && this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void onDeleteByIdExecuted(JoinPoint pjp) {
        onDelete(pjp);
    }

    @AfterReturning("execution(public * delete(..)) && this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void onDeleteExecuted(JoinPoint pjp) {
        onDelete(pjp);
    }

    @AfterReturning("execution(public * deleteBatchIds(..)) && this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void onDeleteBatchIdsExecuted(JoinPoint pjp) {
        onDelete(pjp);
    }

    @AfterReturning(value = "execution(public * insert(..)) && this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void onInsertExecuted(JoinPoint pjp) {
        onSave(pjp);
    }

    @AfterReturning(value = "execution(public * updateById(..)) && this(com.baomidou.mybatisplus.core.mapper.BaseMapper)")
    public void onUpdateByIdExecuted(JoinPoint pjp) {
        onSave(pjp);
    }

    // update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);
    // delete(@Param("ew") Wrapper<T> queryWrapper);
    // deleteByMap(@Param("cm") Map<String, Object> columnMap);
}
