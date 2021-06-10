package in.hocg.boot.task.autoconfiguration.core;

import lombok.Data;

/**
 * Created by hocgin on 2021/5/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class TaskResult<R> {
    private Integer code;
    private String message;
    private R data;

    public static <R> TaskResult<R> fail() {
        return new TaskResult<>();
    }

    public static <R> TaskResult<R> success(R result) {
        return null;
    }
}
