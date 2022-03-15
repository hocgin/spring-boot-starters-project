package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.aspect;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.annotation.MyBatisPlusJaversAuditable;
import org.aspectj.lang.JoinPoint;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.spring.auditable.AspectUtil;
import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.CommitPropertiesProvider;
import org.javers.spring.auditable.aspect.JaversCommitAdvice;

import java.util.Optional;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class AbstractSpringAuditableMapperAspect {
    private final JaversCommitAdvice javersCommitAdvice;
    private final Javers javers;

    protected AbstractSpringAuditableMapperAspect(Javers javers, AuthorProvider authorProvider, CommitPropertiesProvider commitPropertiesProvider) {
        this.javers = javers;
        this.javersCommitAdvice = new JaversCommitAdvice(javers, authorProvider, commitPropertiesProvider);
    }

    protected void onSave(JoinPoint pjp) {
        Object returnedObject = pjp.getArgs()[0];
        onSave(pjp, returnedObject);
    }

    protected void onSave(JoinPoint pjp, Object returnedObject) {
        getRepositoryInterface(pjp).ifPresent(i ->
            AspectUtil.collectReturnedObjects(returnedObject).forEach(javersCommitAdvice::commitObject));
    }

    protected void onDelete(JoinPoint pjp) {
        getRepositoryInterface(pjp).ifPresent(i -> {
            TableInfo metadata = TableInfoHelper.getTableInfo(i.getClass());
            for (Object deletedObject : AspectUtil.collectArguments(pjp)) {
                handleDelete(metadata, deletedObject);
            }
        });
    }

    private Optional<Class> getRepositoryInterface(JoinPoint pjp) {
        for (Class i : pjp.getTarget().getClass().getInterfaces()) {
            if (i.isAnnotationPresent(MyBatisPlusJaversAuditable.class) && BaseMapper.class.isAssignableFrom(i)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    void handleDelete(TableInfo repositoryMetadata, Object domainObjectOrId) {
        if (isIdClass(repositoryMetadata, domainObjectOrId)) {
            Class<?> domainType = repositoryMetadata.getEntityType();
            if (javers.findSnapshots(QueryBuilder.byInstanceId(domainObjectOrId, domainType).limit(1).build()).size() == 0) {
                return;
            }
            javersCommitAdvice.commitShallowDeleteById(domainObjectOrId, domainType);
        } else if (isDomainClass(repositoryMetadata, domainObjectOrId)) {
            if (javers.findSnapshots(QueryBuilder.byInstance(domainObjectOrId).limit(1).build()).size() == 0) {
                return;
            }
            javersCommitAdvice.commitShallowDelete(domainObjectOrId);
        } else {
            throw new IllegalArgumentException("Domain object or object id expected");
        }
    }

    private boolean isDomainClass(TableInfo metadata, Object o) {
        return metadata.getEntityType().isAssignableFrom(o.getClass());
    }

    private boolean isIdClass(TableInfo metadata, Object o) {
        return metadata.getKeyType().isAssignableFrom(o.getClass());
    }
}
