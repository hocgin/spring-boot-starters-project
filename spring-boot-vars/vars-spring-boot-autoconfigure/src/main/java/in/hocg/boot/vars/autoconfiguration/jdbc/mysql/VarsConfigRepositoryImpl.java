package in.hocg.boot.vars.autoconfiguration.jdbc.mysql;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.vars.autoconfiguration.core.VarsConfigRepository;
import in.hocg.boot.vars.autoconfiguration.jdbc.TableVarsConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2021/6/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class VarsConfigRepositoryImpl implements VarsConfigRepository {
    private final DataSource dataSource;

    @Override
    @SneakyThrows(SQLException.class)
    public void set(@NonNull String key, Object value, String title, String remark) {
        LocalDateTime now = LocalDateTime.now();
        Entity entity = Entity.create(TableVarsConfig.TABLE_NAME)
            .set(TableVarsConfig.FIELD_VAR_KEY, key)
            .setIgnoreNull(TableVarsConfig.FIELD_REMARK, remark)
            .setIgnoreNull(TableVarsConfig.FIELD_TITLE, title)
            .setIgnoreNull(TableVarsConfig.FIELD_VAR_VALUE, this.asValue(value));

        Optional<Long> idOpt = this.getIdByKey(key);

        // 更新
        if (idOpt.isPresent()) {
            entity.set(TableVarsConfig.FIELD_LAST_UPDATED_AT, now);
            Entity where = Entity.create(TableVarsConfig.TABLE_NAME).set(TableVarsConfig.FIELD_ID, idOpt.get());
            Db.use(dataSource).update(entity, where);
        }
        // 新增
        else {
            entity.set(TableVarsConfig.FIELD_CREATED_AT, now);
            Db.use(dataSource).insert(entity);
        }
    }

    @Override
    @SneakyThrows(SQLException.class)
    public <T> Optional<T> getValue(String key, Class<T> clazz) {
        Entity entity = Db.use(dataSource).get(
            Entity.create(TableVarsConfig.TABLE_NAME)
                .setFieldNames(TableVarsConfig.FIELD_VAR_VALUE)
                .set(TableVarsConfig.FIELD_VAR_KEY, key));
        String value = entity.getStr(TableVarsConfig.FIELD_VAR_VALUE);
        return Optional.ofNullable(this.asValue(clazz, value));
    }

    @SneakyThrows(SQLException.class)
    private Optional<Long> getIdByKey(String key) {
        return Optional.ofNullable(Db.use(dataSource).get(
            Entity.create(TableVarsConfig.TABLE_NAME)
                .setFieldNames(TableVarsConfig.FIELD_ID)
                .set(TableVarsConfig.FIELD_VAR_KEY, key)))
            .map(entity -> entity.getLong(TableVarsConfig.FIELD_ID));
    }


    private String asValue(Object value) {
        String result;
        if (Objects.isNull(value)) {
            result = null;
        } else if (ClassUtil.isBasicType(value.getClass())) {
            result = Convert.convert(String.class, value);
        } else {
            result = JSONUtil.toJsonStr(value);
        }
        return result;
    }

    private <T> T asValue(Class<T> clazz, String value) {
        T result;
        if (Objects.isNull(value)) {
            result = null;
        } else if (ClassUtil.isBasicType(clazz)) {
            result = Convert.convert(clazz, value);
        } else {
            result = JSONUtil.toBean(value, clazz);
        }
        return result;
    }
}
