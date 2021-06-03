package in.hocg.boot.task.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
    private String params;

    public <R> R resolveParams(Class<R> clazz) {
        if (StrUtil.isBlank(params) || !JSONUtil.isJson(params)) {
            return null;
        }
        return JSONUtil.toBean(params, clazz);
    }
}
