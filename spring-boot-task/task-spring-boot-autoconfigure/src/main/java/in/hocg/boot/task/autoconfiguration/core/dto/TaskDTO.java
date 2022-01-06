package in.hocg.boot.task.autoconfiguration.core.dto;

import in.hocg.boot.task.autoconfiguration.core.entity.TaskInfo;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by hocgin on 2022/1/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class TaskDTO implements Serializable {
    private Long id;
    private String taskSn;
    private String type;
    private String title;
    private String params;
    private Integer retryCount;
    private Item lastTaskItem;

    public static TaskDTO as(TaskInfo taskInfo, TaskItem taskItem) {
        TaskDTO result = new TaskDTO()
            .setId(taskInfo.getId())
            .setTitle(taskInfo.getTitle())
            .setParams(taskInfo.getParams())
            .setTaskSn(taskInfo.getTaskSn())
            .setType(taskInfo.getType())
            .setRetryCount(taskInfo.getRetryCount());
        if (Objects.nonNull(taskItem)) {
            Item item = new Item()
                .setTaskId(taskItem.getTaskId())
                .setStartAt(taskItem.getStartAt())
                .setDoneAt(taskItem.getDoneAt())
                .setReadyAt(taskItem.getReadyAt())
                .setParams(taskItem.getParams())
                .setIdx(taskItem.getIdx())
                .setDoneStatus(taskItem.getDoneStatus())
                .setType(taskItem.getType())
                .setStatus(taskItem.getStatus())
                .setTotalTimeMillis(taskItem.getTotalTimeMillis())
                .setDoneMessage(taskItem.getDoneMessage())
                .setDoneResult(taskItem.getDoneResult())
                .setId(taskItem.getId());
            result.setLastTaskItem(item);
        }

        return result;
    }

    @Data
    public static class Item implements Serializable {
        /**
         * ID
         */
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
         * @see TaskItem.Status
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
    }
}
