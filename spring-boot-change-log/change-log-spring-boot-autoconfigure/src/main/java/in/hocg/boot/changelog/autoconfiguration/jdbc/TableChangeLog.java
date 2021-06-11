package in.hocg.boot.changelog.autoconfiguration.jdbc;

import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/6/10
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TableChangeLog {
    public static final String TABLE_NAME = "boot_change_log";

    public static final String FIELD_LOG_SN = "log_sn";
    public static final String FIELD_REF_TYPE = "ref_type";
    public static final String FIELD_REF_ID = "ref_id";
    public static final String FIELD_CHANGE_TYPE = "change_type";
    public static final String FIELD_CREATOR = "creator";
    public static final String FIELD_CREATED_AT = "created_at";
}
