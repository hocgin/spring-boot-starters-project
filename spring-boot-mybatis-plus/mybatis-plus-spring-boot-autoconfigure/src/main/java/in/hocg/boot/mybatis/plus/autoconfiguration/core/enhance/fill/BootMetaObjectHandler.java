package in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.fill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.UserContextHolder;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.beta.BetaAbstractEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/12/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class BootMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, getFieldName(BetaAbstractEntity<BetaAbstractEntity<?>>::getCreatedAt), LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, getFieldName(BetaAbstractEntity<BetaAbstractEntity<?>>::getCreator), Long.class, UserContextHolder.getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 表静态信息
        TableInfo tableInfo = this.findTableInfo(metaObject);
        Class<?> entityType = tableInfo.getEntityType();
//        metaObject.setValue();
//        metaObject.getValue()

        this.strictUpdateFill(metaObject, getFieldName(BetaAbstractEntity<BetaAbstractEntity<?>>::getLastUpdatedAt), LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, getFieldName(BetaAbstractEntity<BetaAbstractEntity<?>>::getLastUpdater), Long.class, UserContextHolder.getUserId());
    }

    private <T> String getFieldName(SFunction<T, ?> func) {
        return PropertyNamer.methodToProperty(LambdaUtils.resolve(func).getImplMethodName());
    }
}
