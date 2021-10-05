package in.hocg.boot.task.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class TaskInfo {
    private Serializable id;
    private String taskSn;
    private String type;
    private String params;
    private String title;
    private Integer retryCount;
    private LocalDateTime readyAt;

    public <R> R resolveParams(Class<?> clazz) {
        if (StrUtil.isBlank(params) || !JSONUtil.isJson(params)) {
            return null;
        }
        return (R) JSONUtil.toBean(params, clazz);
    }
}
