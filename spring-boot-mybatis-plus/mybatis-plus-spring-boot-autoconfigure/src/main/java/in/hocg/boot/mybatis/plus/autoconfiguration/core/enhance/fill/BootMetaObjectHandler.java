package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.fill;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.UserContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.EntityListeners;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.PreInsert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.listeners.PreUpdate;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

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
    private final ApplicationContext applicationContext;

    @Override
    public void insertFill(MetaObject metaObject) {
        hookEntityListeners(metaObject, true);

        this.strictInsertFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getCreatedAt), LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getCreator), UserContextHolder::getUserId, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        hookEntityListeners(metaObject, false);

        this.strictUpdateFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getLastUpdatedAt), LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, getFieldName(CommonEntity<CommonEntity<?>>::getLastUpdater), UserContextHolder::getUserId, Long.class);
    }

    private void hookEntityListeners(MetaObject metaObject, boolean isInsert) {
        TableInfo tableInfo = this.findTableInfo(metaObject);
        Class<?> entityType = tableInfo.getEntityType();

        // 未使用注解的情况下，直接返回
        if (!entityType.isAnnotationPresent(EntityListeners.class)) {
            return;
        }

        Class<?>[] classes = entityType.getAnnotation(EntityListeners.class).value();
        for (Class<?> aClass : classes) {
            List<Method> methods = ClassUtil.getPublicMethods(aClass, method -> isInsert ? method.isAnnotationPresent(PreInsert.class) : method.isAnnotationPresent(PreUpdate.class));
            if (CollUtil.isEmpty(methods)) {
                break;
            }
            Object bean = applicationContext.getBean(aClass);
            Object originalObject = metaObject.getOriginalObject();
            for (Method method : methods) {
                try {
                    method.invoke(bean, originalObject);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.warn("{} 的 @{} 扩展事件[@{}]调用失败", aClass.getName(), EntityListeners.class.getSimpleName(), isInsert ? PreInsert.class.getSimpleName() : PreUpdate.class.getSimpleName());
                }
            }
        }
    }

    private <T> String getFieldName(SFunction<T, ?> func) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(func).getImplMethodName());
    }
}
