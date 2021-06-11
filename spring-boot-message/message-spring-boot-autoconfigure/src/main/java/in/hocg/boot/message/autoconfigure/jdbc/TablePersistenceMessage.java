package in.hocg.boot.message.autoconfigure.jdbc;

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
public class TablePersistenceMessage {
    public static final String TABLE_NAME = "boot_persistence_message";

    public static final String FIELD_GROUP_SN = "group_sn";
    public static final String FIELD_DESTINATION = "destination";
    public static final String FIELD_PAYLOAD = "payload";
    public static final String FIELD_HEADERS = "headers";
    public static final String FIELD_PUBLISHED = "published";
    public static final String FIELD_PREPARED_AT = "prepared_at";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_LAST_UPDATED_AT = "last_updated_at";

    @Getter
    @RequiredArgsConstructor
    public enum PersistenceMessagePublished {
        Prepare("prepare", "准备就绪"),
        Complete("complete", "已完成");
        private final Serializable code;
        private final String name;
    }
}
