package in.hocg.boot.http.log.autoconfiguration.jdbc;

import in.hocg.boot.utils.enums.ICode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/8/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TableHttpLog {
    public static final String TABLE_NAME = "boot_task_info";

    public static final String FIELD_ID = "id";
    public static final String FIELD_URI = "uri";
    public static final String FIELD_REQUEST_METHOD = "request_method";
    public static final String FIELD_REQUEST_BODY = "request_body";
    public static final String FIELD_REQUEST_HEADERS = "request_headers";
    public static final String FIELD_RESPONSE_HEADERS = "response_headers";
    public static final String FIELD_RESPONSE_BODY = "response_body";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ATTACH = "attach";
    public static final String FIELD_DIRECTION = "direction";
    public static final String FIELD_BE_CALLER = "be_caller";
    public static final String FIELD_CALLER = "caller";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_FAIL_REASON = "fail_reason";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_DONE_AT = "done_at";
    public static final String FIELD_CREATOR = "creator";


    @Getter
    @RequiredArgsConstructor
    public enum Status implements ICode {
        Executing("executing", "执行中"),
        Success("success", "成功"),
        Fail("fail", "失败");
        private final Serializable code;
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Direction implements ICode {
        In("in", "入"),
        Out("out", "出");
        private final Serializable code;
        private final String name;
    }

}
