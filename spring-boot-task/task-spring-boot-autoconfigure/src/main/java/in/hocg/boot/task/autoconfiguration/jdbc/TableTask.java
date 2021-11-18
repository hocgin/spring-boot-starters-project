package in.hocg.boot.task.autoconfiguration.jdbc;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TableTask {
    public static final String TABLE_NAME = "boot_task_info";

    public static final String FIELD_ID = "id";
    public static final String FIELD_TASK_SN = "task_sn";
    public static final String FIELD_RETRY_COUNT = "retry_count";
    public static final String FIELD_RETRY_ID = "retry_id";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_CREATOR = "creator";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_READY_AT = "ready_at";
    public static final String FIELD_START_AT = "start_at";
    public static final String FIELD_DONE_AT = "done_at";
    public static final String FIELD_DONE_STATUS = "done_status";
    public static final String FIELD_DONE_MESSAGE = "done_message";
    public static final String FIELD_DONE_RESULT = "done_result";
    public static final String FIELD_TOTAL_TIME_MILLIS = "total_time_millis";
    public static final String FIELD_PARAMS = "params";

    @Getter
    @RequiredArgsConstructor
    public enum Status implements ICode {
        Ready("ready", "准备完成"),
        Executing("executing", "执行中"),
        Done("done", "结束");
        private final Serializable code;
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DoneStatus implements ICode {
        Success("success", "成功"),
        PartFail("part_fail", "部分失败"),
        Fail("fail", "失败");
        private final Serializable code;
        private final String name;
    }
}
