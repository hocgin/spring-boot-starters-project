package in.hocg.boot.mybatis.plus.extensions.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import in.hocg.boot.utils.enums.ICode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(TaskItem.TABLE_NAME)
public class TaskItem extends CommonEntity<TaskItem> {
    public static final String TABLE_NAME = MyBatisPlusExtensionsConstants.TABLE_PREFIX + "task_item";

    @TableField("task_id")
    private Long taskId;
    @TableField("type")
    private String type;
    @TableField("status")
    private String status;
    @TableField("params")
    private String params;
    @TableField("idx")
    private Integer idx;
    @TableField("done_status")
    private String doneStatus;
    @TableField("done_message")
    private String doneMessage;
    @TableField("done_result")
    private String doneResult;
    @TableField("total_time_millis")
    private Long totalTimeMillis;
    @TableField("ready_at")
    private LocalDateTime readyAt;
    @TableField("start_at")
    private LocalDateTime startAt;
    @TableField("done_at")
    private LocalDateTime doneAt;


    @Getter
    @RequiredArgsConstructor
    public enum Status implements ICode {
        Ready("ready", "准备完成"),
        Executing("executing", "执行中"),
        Done("done", "结束");
        private final String code;
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DoneStatus implements ICode {
        Success("success", "成功"),
        PartFail("part_fail", "部分失败"),
        Expired("expired", "超时过期"),
        Fail("fail", "失败");
        private final String code;
        private final String name;
    }
}
