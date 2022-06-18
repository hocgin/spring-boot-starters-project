package in.hocg.boot.mybatis.plus.extensions.task.entity;

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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(TaskInfo.TABLE_NAME)
public class TaskInfo extends CommonEntity<TaskInfo> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "task_info";

    @TableField("task_sn")
    private String taskSn;
    @TableField("title")
    private String title;
    @TableField("type")
    private String type;
    @TableField("params")
    private String params;
    @TableField("retry_count")
    private Integer retryCount;
}
