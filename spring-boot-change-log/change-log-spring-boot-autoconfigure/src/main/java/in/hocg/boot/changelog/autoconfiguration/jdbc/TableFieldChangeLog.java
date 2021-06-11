package in.hocg.boot.changelog.autoconfiguration.jdbc;

import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TableFieldChangeLog {
    public static final String TABLE_NAME = "boot_field_change_log";

    public static final String FIELD_CHANGE_LOG_ID = "change_log_id";
    public static final String FIELD_FIELD_NAME = "field_name";
    public static final String FIELD_FIELD_REMARK = "field_remark";
    public static final String FIELD_CHANGE_REMARK = "change_remark";
    public static final String FIELD_BEFORE_VALUE = "before_value";
    public static final String FIELD_AFTER_VALUE = "after_value";

}
