package in.hocg.boot.cache.autoconfiguration.tree;


import in.hocg.boot.cache.autoconfiguration.AbstractService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2020/3/14.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TreeService<T> extends AbstractService<T> {

    /**
     * 获取指定节点的父节点
     *
     * @param id id
     * @return
     */
    Optional<T> getParent(Long id);

    /**
     * 删除指定节点及其子节点
     *
     * @param id id
     */
    void deleteCurrentAndChildren(Serializable id);

    /**
     * 查询指定下级
     *
     * @param parentId
     * @param enabled
     * @return
     */
    List<T> selectListByParentId(Serializable parentId, Integer enabled);
}
