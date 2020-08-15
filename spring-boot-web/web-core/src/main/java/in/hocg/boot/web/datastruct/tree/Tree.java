package in.hocg.boot.web.datastruct.tree;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2020/2/15.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public final class Tree {

    /**
     * 构建树的必须条件
     *
     * @param <T>
     */
    public interface Node<T extends Node> {
        Long getId();

        Long getParentId();

        void setChildren(List<T> children);
    }


    /**
     * 获取树
     *
     * @param id
     * @param rootMenu
     * @param <T>
     * @return
     */
    public static <T extends Node> List<T> getChild(Long id, Collection<T> rootMenu) {
        // 子菜单
        List<T> children = new ArrayList<>();
        for (T menu : rootMenu) {
            final Long parentId = menu.getParentId();
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (equals(id, parentId)) {
                children.add(menu);
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (T menu : children) {
            menu.setChildren(getChild(menu.getId(), rootMenu));
        }
        // 递归退出条件
        if (children.isEmpty()) {
            return children;
        }
        return children;
    }


    private static boolean equals(Long s1, Long s2) {
        if (s1 == null || s2 == null) {
            return Objects.equals(s1, s2);
        }

        return s1.compareTo(s2) == 0;
    }
}
