package in.hocg.boot.mybatis.plus.extensions.changelog;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.changelog.compare.EntityCompare;
import in.hocg.boot.mybatis.plus.extensions.changelog.enums.ChangeType;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.ChangeLogDto;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.FieldChangeDto;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;
import in.hocg.boot.utils.lambda.SFunction;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ChangeLogMpeService {

    @Transactional(rollbackFor = Exception.class)
    void record(ChangeLogDto dto);

    @Transactional(rollbackFor = Exception.class)
    default void updateLog(@NonNull String refType, @NonNull Long refId, List<FieldChangeDto> changes) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeType.Modify);
        result.setRefId(refId);
        result.setRefType(refType);
        result.setChange(changes);
        this.record(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void deleteLog(@NonNull String refType, @NonNull Long refId) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeType.Delete);
        result.setRefId(refId);
        result.setRefType(refType);
        result.setChange(Collections.emptyList());
        this.record(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default void insertLog(@NonNull String refType, @NonNull Long refId) {
        insertLog(refType, refId, Collections.emptyList());
    }

    @Transactional(rollbackFor = Exception.class)
    default void insertLog(@NonNull String refType, @NonNull Long refId, List<FieldChangeDto> change) {
        final ChangeLogDto result = new ChangeLogDto();
        result.setChangeType(ChangeType.Insert);
        result.setRefId(refId);
        result.setRefType(refType);
        result.setChange(change);
        this.record(result);
    }

    @Transactional(rollbackFor = Exception.class)
    default <T extends Model<?>> T record(ChangeType type, String refType, T newModel) {
        List<SFunction<? extends CommonEntity<?>, ?>> ignoreFields = Collections.emptyList();
        if (newModel instanceof CommonEntity) {
            ignoreFields = List.of(CommonEntity::getCreatedAt, CommonEntity::getCreator);
        }

        if (type.eq(ChangeType.Delete)) {
            this.deleteLog(refType, Convert.toLong(newModel.pkVal()));
            newModel.deleteById();
        } else if (type.eq(ChangeType.Insert)) {
            newModel.insert();
            this.insertLog(refType, Convert.toLong(newModel.pkVal()), EntityCompare.diffNotNull(newModel, ignoreFields.toArray(new SFunction[0])));
        } else if (type.eq(ChangeType.Modify)) {
            List<FieldChangeDto> changes = EntityCompare.diffNotNull(newModel.selectById(newModel.pkVal()), newModel, ignoreFields.toArray(new SFunction[0]));
            this.updateLog(refType, Convert.toLong(newModel.pkVal()), changes);
            newModel.updateById();
        }
        return newModel;
    }

    IScroll<ChangeVo> scroll(ChangeLogScrollRo ro);
}
