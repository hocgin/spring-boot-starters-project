package in.hocg.boot.vars.autoconfiguration.jdbc;

import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/6/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class TableVarsConfig {
    public static final String TABLE_NAME = "boot_vars_config";

    public static final String FIELD_ID = "id";
    public static final String FIELD_VAR_KEY = "var_key";
    public static final String FIELD_VAR_VALUE = "var_value";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_CREATOR = "creator";
    public static final String FIELD_LAST_UPDATED_AT = "last_updated_at";
    public static final String FIELD_LAST_UPDATER = "last_updater";
}
