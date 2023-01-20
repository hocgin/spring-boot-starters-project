package in.hocg.boot.mybatis.plus.extensions.userconfig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * [BOOT] 用户配置表
 * </p>
 *
 * @author hocgin
 * @since 2023-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("boot_user_config")
public class UserConfig extends CommonEntity<UserConfig> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private Long userId;
    @ApiModelProperty("域,如: 项目编号")
    @TableField("scope")
    private String scope;
    @ApiModelProperty("配置项")
    @TableField("code")
    private String code;
    @ApiModelProperty("配置值")
    @TableField("value")
    private String value;



}
