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
@TableName(Change.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Change extends CommonEntity<Change> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "change";

    @TableField("log_sn")
    private String logSn;
    @TableField("ref_type")
    private String refType;
    @TableField("ref_id")
    private Long refId;
    @TableField("change_type")
    private String changeType;
}
