package in.hocg.boot.mybatis.plus.extensions.config.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(ConfigItem.TABLE_NAME)
public class ConfigItem extends CommonEntity<ConfigItem> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "config_item";

    @ApiModelProperty
    @TableField("scope_id")
    private Long scopeId;
    @ApiModelProperty
    @TableField("title")
    private String title;
    @TableField("remark")
    private String remark;
    @ApiModelProperty
    @TableField("name")
    private String name;
    @ApiModelProperty
    @TableField("type")
    private String type;
    @ApiModelProperty
    @TableField("default_value")
    private String defaultValue;
    @ApiModelProperty
    @TableField("readable")
    private Boolean readable;
    @ApiModelProperty
    @TableField("writable")
    private Boolean writable;
    @ApiModelProperty
    @TableField("nullable")
    private Boolean nullable;
}
