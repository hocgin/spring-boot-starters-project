package in.hocg.boot.mybatis.plus.sample.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.LogicDeletedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_example")
public class Example extends LogicDeletedEntity<Example> {
    private static final long serialVersionUID = 1L;

    @TableField("name")
    private String name;
    @TableField("created_at")
    private LocalDateTime createdAt;

}
