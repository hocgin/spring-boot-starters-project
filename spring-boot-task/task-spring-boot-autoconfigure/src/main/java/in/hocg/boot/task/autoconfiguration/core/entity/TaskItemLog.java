package in.hocg.boot.task.autoconfiguration.core.entity;

import cn.hutool.db.Entity;
import in.hocg.boot.task.autoconfiguration.utils.TaskUtils;
import in.hocg.boot.utils.LambdaUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class TaskItemLog {
    public static final String TABLE_NAME = "boot_task_item_log";
    private Long id;
    /**
     * 任务项
     */
    private Long taskItemId;
    /**
     * 行日志信息
     */
    private String content;

    private LocalDateTime createdAt;
    private Long creator;
    private LocalDateTime lastUpdatedAt;
    private Long lastUpdater;

    public static TaskItemLog as(Entity entity) {
        return new TaskItemLog().setId(entity.getLong(LambdaUtils.getColumnName(TaskItemLog::getId)))
            .setTaskItemId(entity.getLong(LambdaUtils.getColumnName(TaskItemLog::getTaskItemId)))
            .setContent(entity.getStr(LambdaUtils.getColumnName(TaskItemLog::getContent)))
            .setCreatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItemLog::getCreatedAt)))
            .setCreator(entity.getLong(LambdaUtils.getColumnName(TaskItemLog::getCreator)))
            .setLastUpdatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItemLog::getLastUpdatedAt)))
            .setLastUpdater(entity.getLong(LambdaUtils.getColumnName(TaskItemLog::getLastUpdater)));
    }

    public Entity asEntity() {
        return Entity.create(TaskItemLog.TABLE_NAME)
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getId), getId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getContent), getContent())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getTaskItemId), getTaskItemId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getCreator), getCreator())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getCreatedAt), getCreatedAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getLastUpdater), getLastUpdater())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItemLog::getLastUpdatedAt), getLastUpdatedAt());
    }
}
