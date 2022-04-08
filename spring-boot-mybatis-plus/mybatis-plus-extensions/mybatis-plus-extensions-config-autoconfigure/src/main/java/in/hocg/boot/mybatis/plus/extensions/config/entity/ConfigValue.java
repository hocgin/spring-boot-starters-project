package in.hocg.boot.mybatis.plus.extensions.config.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(ConfigValue.TABLE_NAME)
public class ConfigValue extends CommonEntity<ConfigValue> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "config_value";

    @ApiModelProperty
    @TableField("item_id")
    private Long itemId;
    @ApiModelProperty
    @TableField("ref_id")
    private Long refId;
    @ApiModelProperty
    @TableField("value")
    private String value;
}
