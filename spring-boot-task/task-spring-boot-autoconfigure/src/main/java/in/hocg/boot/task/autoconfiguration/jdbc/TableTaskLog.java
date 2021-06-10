package in.hocg.boot.task.autoconfiguration.jdbc;

import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TableTaskLog {
    public static final String TABLE_NAME = "boot_task_log";

    public static final String FIELD_TASK_ID = "task_id";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_CREATED_AT = "created_at";
}
