package in.hocg.boot.mybatis.plus.extensions.task.support;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskResult<R> implements Serializable {
    private Boolean success;
    private String message;
    private R data;

    public static <T> TaskResult<T> result(boolean success) {
        TaskResult<T> result = new TaskResult<>();
        return result.setSuccess(success);
    }

    public static <R> TaskResult<R> fail() {
        return TaskResult.result(false);
    }

    public static <R> TaskResult<R> fail(String message) {
        TaskResult<R> result = TaskResult.fail();
        return result.setMessage(message);
    }

    public static <R> TaskResult<R> success() {
        return TaskResult.result(true);
    }

    public static <R> TaskResult<R> success(R data) {
        TaskResult<R> result = TaskResult.success();
        return result.setData(data);
    }
}
