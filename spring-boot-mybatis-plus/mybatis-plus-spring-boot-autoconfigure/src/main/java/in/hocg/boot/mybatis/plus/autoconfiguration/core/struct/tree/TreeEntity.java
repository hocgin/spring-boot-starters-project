package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.tree;

import com.baomidou.mybatisplus.annotation.TableField;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.ColumnConstants;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2020/3/14.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public abstract class TreeEntity<T extends TreeEntity<?>> extends CommonEntity<T> {
    /**
     * 父级ID, 顶级为 NULL
     */
    @TableField(ColumnConstants.PARENT_ID)
    private Long parentId;
    /**
     * 树路径，组成方式: /父路径/当前ID
     */
    @TableField(ColumnConstants.TREE_PATH)
    private String treePath;
    /**
     * 启用状态
     */
    @TableField(ColumnConstants.ENABLED)
    private Boolean enabled;

}
