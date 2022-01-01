package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance;

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
public abstract class CommonEntity<T extends CommonEntity<?>> extends AbstractEntity<T> {
    @TableId(value = ColumnConstants.ID)
    private Long id;

    @TableField(value = ColumnConstants.CREATED_AT, fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = ColumnConstants.CREATOR, fill = FieldFill.INSERT)
    private Long creator;

    @TableField(value = ColumnConstants.LAST_UPDATED_AT, fill = FieldFill.UPDATE)
    private LocalDateTime lastUpdatedAt;
    @TableField(value = ColumnConstants.LAST_UPDATER, fill = FieldFill.UPDATE)
    private Long lastUpdater;

}
