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
public class TaskInfo {
    public static final String TABLE_NAME = "boot_task_info";
    private Long id;
    /**
     * 任务编号
     */
    private String taskSn;
    /**
     * 任务类型
     */
    private String type;
    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务参数
     */
    private String params;
    /**
     * 当前执行次数
     */
    private Integer retryCount;

    private LocalDateTime createdAt;
    private Long creator;
    private LocalDateTime lastUpdatedAt;
    private Long lastUpdater;

    public static TaskInfo as(Entity entity) {
        return new TaskInfo().setId(entity.getLong(LambdaUtils.getColumnName(TaskInfo::getId)))
            .setTaskSn(entity.getStr(LambdaUtils.getColumnName(TaskInfo::getTaskSn)))
            .setType(entity.getStr(LambdaUtils.getColumnName(TaskInfo::getType)))
            .setTitle(entity.getStr(LambdaUtils.getColumnName(TaskInfo::getTitle)))
            .setParams(entity.getStr(LambdaUtils.getColumnName(TaskInfo::getParams)))
            .setRetryCount(entity.getInt(LambdaUtils.getColumnName(TaskInfo::getRetryCount)))
            .setCreatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskInfo::getCreatedAt)))
            .setCreator(entity.getLong(LambdaUtils.getColumnName(TaskInfo::getCreator)))
            .setLastUpdatedAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskInfo::getLastUpdatedAt)))
            .setLastUpdater(entity.getLong(LambdaUtils.getColumnName(TaskInfo::getLastUpdater)));
    }

    public Entity asEntity() {
        return Entity.create(TaskInfo.TABLE_NAME)
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getId), getId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getTaskSn), getTaskSn())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getType), getType())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getTitle), getTitle())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getParams), getParams())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getRetryCount), getRetryCount())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getCreator), getCreator())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getCreatedAt), getCreatedAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getLastUpdater), getLastUpdater())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskInfo::getLastUpdatedAt), getLastUpdatedAt());
    }

}
