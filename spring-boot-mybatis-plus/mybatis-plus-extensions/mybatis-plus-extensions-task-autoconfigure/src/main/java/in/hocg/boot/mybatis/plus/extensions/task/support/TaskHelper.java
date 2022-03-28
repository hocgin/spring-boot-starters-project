package in.hocg.boot.mybatis.plus.extensions.task.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TaskHelper {

    public <R> R resolveParams(TaskItem taskInfo, Class<?> clazz) {
        String params = taskInfo.getParams();
        if (StrUtil.isBlank(params) || !JSONUtil.isJson(params)) {
            return null;
        }
        return (R) JSONUtil.toBean(params, clazz);
    }
}
