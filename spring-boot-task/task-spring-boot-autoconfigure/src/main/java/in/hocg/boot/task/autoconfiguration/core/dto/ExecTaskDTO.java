package in.hocg.boot.task.autoconfiguration.core.dto;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.task.autoconfiguration.core.entity.TaskItem;
import in.hocg.boot.task.autoconfiguration.utils.TaskUtils;
import in.hocg.boot.utils.LambdaUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class ExecTaskDTO {
    private Long id;
    private Long taskId;
    private String type;
    private String params;
    private Integer idx;
    private LocalDateTime readyAt;

    public <R> R resolveParams(Class<?> clazz) {
        if (StrUtil.isBlank(params) || !JSONUtil.isJson(params)) {
            return null;
        }
        return (R) JSONUtil.toBean(params, clazz);
    }

    public static ExecTaskDTO as(Entity entity) {
        return new ExecTaskDTO().setId(entity.getLong(LambdaUtils.getColumnName(TaskItem::getId)))
            .setType(entity.getStr(LambdaUtils.getColumnName(TaskItem::getType)))
            .setTaskId(entity.getLong(LambdaUtils.getColumnName(TaskItem::getTaskId)))
            .setIdx(entity.getInt(LambdaUtils.getColumnName(TaskItem::getIdx)))
            .setParams(entity.getStr(LambdaUtils.getColumnName(TaskItem::getParams)))
            .setReadyAt(TaskUtils.getLocalDateTime(entity, LambdaUtils.getColumnName(TaskItem::getReadyAt)));
    }
}
