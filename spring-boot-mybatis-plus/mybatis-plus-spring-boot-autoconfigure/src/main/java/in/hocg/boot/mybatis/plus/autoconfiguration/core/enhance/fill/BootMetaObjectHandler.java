package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.fill;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.MybatisContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.EntityListener;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.EntityListeners;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 * <br/>
 * 职责:
 * <br/>
 * 1. 填充基础字段
 * <br/>
 * - 填充创建人、创建时间
 * <br/>
 * - 填充修改人、修改时间
 * <br/>
 * 2. 触发实体监听器
 * <br/>
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class BootMetaObjectHandler implements MetaObjectHandler {
    private final MybatisContextHolder contextHolder;

    @Override
    public void insertFill(MetaObject metaObject) {
        hookEntityListeners(metaObject, true);

        this.strictInsertFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getCreatedAt), LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getCreator), contextHolder::getUserId, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        hookEntityListeners(metaObject, false);

        this.strictUpdateFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getLastUpdatedAt), LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getLastUpdater), contextHolder::getUserId, Long.class);
    }

    private void hookEntityListeners(MetaObject metaObject, boolean isInsert) {
        TableInfo tableInfo = this.findTableInfo(metaObject);
        Class<?> entityType = tableInfo.getEntityType();

        // 未使用注解的情况下，直接返回
        if (!entityType.isAnnotationPresent(EntityListeners.class)) {
            return;
        }
        Class<? extends EntityListener>[] classes = entityType.getAnnotation(EntityListeners.class).value();
        for (Class<? extends EntityListener> clazz : classes) {
            Object originalObject = metaObject.getOriginalObject();
            EntityListener bean = SpringUtil.getBean(clazz);
            if (isInsert) {
                bean.onPreInsert(originalObject);
            } else {
                bean.onPreUpdate(originalObject);
            }
        }
    }

    private <T> String getFieldName(SFunction<T, ?> func) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(func).getImplMethodName());
    }
}
