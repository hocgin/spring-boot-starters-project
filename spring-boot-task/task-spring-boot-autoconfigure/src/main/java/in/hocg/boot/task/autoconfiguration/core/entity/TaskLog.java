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
public class TaskLog {
    public static final String TABLE_NAME = "boot_task_log";
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

    public static TaskLog as(Entity entity) {
        return new TaskLog().setId(entity.getLong(LambdaUtils.getColumnName(TaskLog::getId)))
            .setTaskItemId(entity.getLong(LambdaUtils.getColumnName(TaskLog::getTaskItemId)))
            .setContent(entity.getStr(LambdaUtils.getColumnName(TaskLog::getContent)))
            .setCreatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskLog::getCreatedAt)))
            .setCreator(entity.getLong(LambdaUtils.getColumnName(TaskLog::getCreator)))
            .setLastUpdatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskLog::getLastUpdatedAt)))
            .setLastUpdater(entity.getLong(LambdaUtils.getColumnName(TaskLog::getLastUpdater)));
    }

    public Entity asEntity() {
        return Entity.create(TaskLog.TABLE_NAME)
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getId), getId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getContent), getContent())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getTaskItemId), getTaskItemId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getCreator), getCreator())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getCreatedAt), getCreatedAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getLastUpdater), getLastUpdater())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskLog::getLastUpdatedAt), getLastUpdatedAt());
    }
}
