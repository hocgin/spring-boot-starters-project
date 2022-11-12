package in.hocg.boot.mybatis.plus.extensions.changelog.convert;

import in.hocg.boot.mybatis.plus.extensions.changelog.entity.Change;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;
import in.hocg.boot.mybatis.plus.extensions.changelog.enums.ChangeType;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.ChangeLogDto;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.FieldChangeDto;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;
import in.hocg.boot.utils.enums.ICode;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ChangeConvert {
    public ChangeVo asChangeVo(Change change, Map<Long, List<ChangeField>> maps) {
        ChangeVo result = new ChangeVo();
        result.setId(change.getId())
            .setLogSn(change.getLogSn())
            .setRefId(change.getRefId())
            .setRefType(change.getRefType())
            .setChange(ChangeConvert.asChangeItem(maps.getOrDefault(change.getId(), Collections.emptyList())))
            .setChangeType(ICode.ofThrow(change.getChangeType(), ChangeType.class));
        return result;
    }

    private List<FieldChangeDto> asChangeItem(List<ChangeField> list) {
        return list.stream().map(changeField -> new FieldChangeDto()
            .setFieldRemark(changeField.getFieldRemark())
            .setBeforeValue(changeField.getBeforeValue())
            .setAfterValue(changeField.getAfterValue())
            .setFieldName(changeField.getFieldName())
            .setChangeRemark(changeField.getChangeRemark())).collect(Collectors.toList());
    }

    public ChangeField asChangeField(FieldChangeDto dto) {
        return new ChangeField()
            .setFieldName(dto.getFieldName())
            .setFieldRemark(dto.getFieldRemark())
            .setChangeRemark(dto.getChangeRemark())
            .setBeforeValue(dto.getBeforeValue())
            .setAfterValue(dto.getAfterValue());
    }

    public Change asChange(ChangeLogDto dto) {
        return new Change()
            .setChangeType(dto.getChangeType().getCodeStr())
            .setRefType(dto.getRefType())
            .setRefId(dto.getRefId());
    }
}
