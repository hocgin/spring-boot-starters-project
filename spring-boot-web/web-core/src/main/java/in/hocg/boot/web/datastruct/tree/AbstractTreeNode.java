package in.hocg.boot.web.datastruct.tree;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/3/18.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public abstract class AbstractTreeNode<T extends AbstractTreeNode> implements Tree.Node<T> {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("名称")
    private String title;
    @ApiModelProperty("父ID")
    private Long parentId;
    @ApiModelProperty("树节点")
    private List<T> children = Collections.emptyList();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public void setChildren(List<T> children) {
        this.children = children;
    }
}
