package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.beta;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.ColumnConstants;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BetaAbstractEntity<T extends BetaAbstractEntity<?>> extends AbstractEntity<T> {
    @TableId(value = BetaAbstractEntity.ID)
    private Long id;

    @TableField(value = BetaAbstractEntity.CREATED_AT, fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = BetaAbstractEntity.CREATOR, fill = FieldFill.INSERT)
    private Long creator;

    @TableField(value = BetaAbstractEntity.LAST_UPDATED_AT, fill = FieldFill.UPDATE)
    private LocalDateTime lastUpdatedAt;
    @TableField(value = BetaAbstractEntity.LAST_UPDATER, fill = FieldFill.UPDATE)
    private Long lastUpdater;

    public static final String ID = "id";
    public static final String TENANT_ID = ColumnConstants.TENANT_ID;
    public static final String DELETED_AT = "deleted_at";
    public static final String DELETER = "deleter";
    public static final String CREATED_AT = "created_at";
    public static final String CREATOR = "creator";
    public static final String LAST_UPDATED_AT = "last_updated_at";
    public static final String LAST_UPDATER = "last_updater";
}
