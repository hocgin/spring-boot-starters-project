package in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.tree;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractEntity;
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
public abstract class TreeEntity<T extends TreeEntity<?>> extends AbstractEntity<T> {
    @TableId(value = TreeEntity.ID, type = IdType.AUTO)
    private Long id;
    /**
     * 父级ID, 顶级为 NULL
     */
    @TableField(TreeEntity.PARENT_ID)
    private Long parentId;
    /**
     * 树路径，组成方式: /父路径/当前ID
     */
    @TableField(TreeEntity.TREE_PATH)
    private String treePath;
    /**
     * 启用状态
     */
    @TableField(TreeEntity.ENABLED)
    private Boolean enabled;

    public static final String ID = "id";
    public static final String PARENT_ID = "parent_id";
    public static final String TREE_PATH = "tree_path";
    public static final String ENABLED = "enabled";
}
