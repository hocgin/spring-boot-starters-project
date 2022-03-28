package in.hocg.boot.mybatis.plus.extensions.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.GlobalConstants;
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
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(TaskItemLog.TABLE_NAME)
public class TaskItemLog extends CommonEntity<TaskItemLog> {
    public static final String TABLE_NAME = GlobalConstants.TABLE_PREFIX + "task_item_log";

    @TableField("task_item_id")
    private Long taskItemId;
    @TableField("content")
    private String content;
}
