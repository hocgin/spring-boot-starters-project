package in.hocg.boot.mybatis.plus.extensions.task.pojo.dto;

import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskInfoItemDTO {
    private Long id;
    private String taskSn;
    private String type;
    private String title;
    private String params;
    private Integer retryCount;

    private TaskItem taskItem;

    public static TaskInfoItemDTO as(TaskInfo taskInfo, TaskItem taskItem) {
        return new TaskInfoItemDTO()
            .setId(taskInfo.getId())
            .setType(taskInfo.getType())
            .setTitle(taskInfo.getTitle())
            .setRetryCount(taskInfo.getRetryCount())
            .setParams(taskInfo.getParams())
            .setTaskItem(taskItem);
    }
}
