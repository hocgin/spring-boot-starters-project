package in.hocg.boot.task.autoconfiguration.core.entity;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.utils.LambdaUtils;
import in.hocg.boot.utils.db.DbUtils;
import in.hocg.boot.utils.enums.ICode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class TaskItem {
    public static final String TABLE_NAME = "boot_task_item";

    private Long id;
    /**
     * 关联任务
     */
    private Long taskId;
    /**
     * 任务类型
     */
    private String type;
    /**
     * 任务状态
     *
     * @see Status
     */
    private String status;
    /**
     * 任务参数
     */
    private String params;
    /**
     * 任务下标(任务的第几次执行)
     */
    private Integer idx;
    /**
     * 执行完成状态
     */
    private String doneStatus;
    /**
     * 执行完成消息
     */
    private String doneMessage;
    /**
     * 执行完成结果
     */
    private String doneResult;
    /**
     * 执行完成耗时(ms)
     */
    private Long totalTimeMillis;
    /**
     * 准备时间
     */
    private LocalDateTime readyAt;
    /**
     * 开始时间
     */
    private LocalDateTime startAt;
    /**
     * 完成时间
     */
    private LocalDateTime doneAt;

    private LocalDateTime createdAt;
    private Long creator;
    private LocalDateTime lastUpdatedAt;
    private Long lastUpdater;


    public <R> R resolveParams(Class<?> clazz) {
        if (StrUtil.isBlank(params) || !JSONUtil.isJson(params)) {
            return null;
        }
        return (R) JSONUtil.toBean(params, clazz);
    }

    public static TaskItem as(Entity entity) {
        return new TaskItem().setId(entity.getLong(LambdaUtils.getColumnName(TaskItem::getId)))
            .setTaskId(entity.getLong(LambdaUtils.getColumnName(TaskItem::getTaskId)))
            .setType(entity.getStr(LambdaUtils.getColumnName(TaskItem::getType)))
            .setStatus(entity.getStr(LambdaUtils.getColumnName(TaskItem::getStatus)))
            .setParams(entity.getStr(LambdaUtils.getColumnName(TaskItem::getParams)))
            .setIdx(entity.getInt(LambdaUtils.getColumnName(TaskItem::getIdx)))
            .setDoneStatus(entity.getStr(LambdaUtils.getColumnName(TaskItem::getDoneStatus)))
            .setDoneMessage(entity.getStr(LambdaUtils.getColumnName(TaskItem::getDoneMessage)))
            .setDoneResult(entity.getStr(LambdaUtils.getColumnName(TaskItem::getDoneResult)))
            .setTotalTimeMillis(entity.getLong(LambdaUtils.getColumnName(TaskItem::getTotalTimeMillis)))
            .setReadyAt(DbUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getReadyAt)))
            .setStartAt(DbUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getStartAt)))
            .setDoneAt(DbUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getDoneAt)))

            .setCreatedAt(DbUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getCreatedAt)))
            .setCreator(entity.getLong(LambdaUtils.getColumnName(TaskItem::getCreator)))
            .setLastUpdatedAt(DbUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getLastUpdatedAt)))
            .setLastUpdater(entity.getLong(LambdaUtils.getColumnName(TaskItem::getLastUpdater)));
    }

    public Entity asEntity() {
        return Entity.create(TaskItem.TABLE_NAME)
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getId), getId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getTaskId), getTaskId())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getType), getType())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getStatus), getStatus())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getParams), getParams())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getIdx), getIdx())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneStatus), getDoneStatus())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneMessage), getDoneMessage())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneResult), getDoneResult())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getTotalTimeMillis), getTotalTimeMillis())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getReadyAt), getReadyAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getStartAt), getStartAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getDoneAt), getDoneAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getCreator), getCreator())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getCreatedAt), getCreatedAt())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getLastUpdater), getLastUpdater())
            .setIgnoreNull(LambdaUtils.getColumnName(TaskItem::getLastUpdatedAt), getLastUpdatedAt());
    }


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
        Fail("fail", "失败");
        private final String code;
        private final String name;
    }
}
