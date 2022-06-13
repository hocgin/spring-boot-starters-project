package in.hocg.boot.mybatis.plus.extensions.changelog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@TableName(ChangeField.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ChangeField extends CommonEntity<ChangeField> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "change_field";

    @TableField("change_id")
    private Long changeId;
    @TableField("field_name")
    private String fieldName;
    @TableField("field_remark")
    private String fieldRemark;
    @TableField("change_remark")
    private String changeRemark;
    @TableField("before_value")
    private String beforeValue;
    @TableField("after_value")
    private String afterValue;
}
