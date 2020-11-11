package in.hocg.boot.mybatis.plus.autoconfiguration.tree;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.autoconfiguration.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.utils.Enabled;
import in.hocg.boot.utils.ValidUtils;
import in.hocg.boot.utils.LangUtils;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2020/3/14.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class TreeServiceImpl<M extends BaseMapper<T>, T extends TreeEntity<?>> extends AbstractServiceImpl<M, T>
    implements TreeService<T> {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validInsert(T entity) {
        validEntity(entity);
        entity.setTreePath("/tmp");
        save(entity);
        final Long parentId = entity.getParentId();
        String treePath = "";
        if (Objects.nonNull(parentId)) {
            final T parent = getById(parentId);
            treePath = parent.getTreePath();
        }
        entity.setTreePath(treePath + "/" + entity.getId());
        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean validUpdateById(T entity) {
        validEntity(entity);
        final Long id = entity.getId();
        final Long newParentId = entity.getParentId();

        // 如果更改了父级
        final T fullEntity = getById(id);
        final Long oldParentId = fullEntity.getParentId();
        if (!LangUtils.equals(newParentId, oldParentId)) {
            String newTreePath = "/" + id;
            String oldTreePath = fullEntity.getTreePath();
            if (Objects.nonNull(newParentId)) {
                final T parent = baseMapper.selectById(newParentId);
                newTreePath = parent.getTreePath() + newTreePath;
            }
            updateTreePathByRightLikeTreePath(oldTreePath, oldTreePath, newTreePath);
        }

        // 如果关闭了状态
        final Integer enabled = entity.getEnabled();
        if (Objects.nonNull(enabled)
            && Enabled.Off.eq(enabled)) {
            updateOffStatusByRightLikeTreePath(fullEntity.getTreePath());
        }

        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void validEntity(T entity) {
        final Long id = entity.getId();
        final Long parentId = entity.getParentId();
        final Integer enabled = entity.getEnabled();

        // 检查父级
        if (Objects.nonNull(parentId)) {
            ValidUtils.isFalse(LangUtils.equals(parentId, id), "父级不能设置为自己");
            T parent = getById(parentId);
            ValidUtils.notNull(parent, "父级不存在");
            ValidUtils.isFalse(parent.getTreePath().contains("/" + id + "/"), "父级不能为自己的子级");
        }

        // 检查开启状态
        if (Objects.nonNull(enabled)
            && Enabled.On.eq(enabled)) {
            Optional<T> parentOpt = Optional.empty();
            if (Objects.nonNull(parentId)) {
                parentOpt = Optional.ofNullable(getById(parentId));
            } else if (Objects.nonNull(id)) {
                parentOpt = getParent(id);
            }

            if (parentOpt.isPresent()) {
                boolean parentIsOff = Enabled.Off.eq(parentOpt.get().getEnabled());
                boolean nowIsOn = Enabled.On.eq(enabled);
                ValidUtils.isFalse(parentIsOff && nowIsOn, "父级为禁用状态，子级不能为开启状态");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<T> getParent(Long id) {
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        final T entity = getById(id);
        if (Objects.isNull(entity)) {
            return Optional.empty();
        }

        final Long parentId = entity.getParentId();
        if (Objects.isNull(parentId)) {
            return Optional.empty();
        }

        final T parent = getById(parentId);
        if (Objects.isNull(parent)) {
            return Optional.empty();
        }
        return Optional.of(parent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCurrentAndChildren(Serializable id) {
        final T entity = getById(id);
        if (Objects.isNull(entity)) {
            return;
        }
        final String treePath = entity.getTreePath();
        final UpdateWrapper<T> deleteWrapper = new UpdateWrapper<>();
        deleteWrapper.likeRight(TreeEntity.TREE_PATH, treePath);
        remove(deleteWrapper);
    }

    @Override
    public List<T> selectListByParentId(Serializable parentId, Integer enabled) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (Objects.isNull(parentId)) {
            queryWrapper.isNull(TreeEntity.PARENT_ID);
        } else {
            queryWrapper.eq(TreeEntity.PARENT_ID, parentId);
        }
        queryWrapper.eq(Objects.nonNull(enabled), TreeEntity.ENABLED, enabled);
        return list(queryWrapper);
    }

    /**
     * 更新树路径
     *
     * @param rightRightTreePath
     * @param oldTreePath
     * @param newTreePath
     */
    private void updateTreePathByRightLikeTreePath(@NonNull String rightRightTreePath,
                                                   @NonNull String oldTreePath,
                                                   @NonNull String newTreePath) {
        final UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.likeRight(TreeEntity.TREE_PATH, rightRightTreePath);
        updateWrapper.setSql(String.format("%s = REPLACE(tree_path, '%s', '%s')", TreeEntity.TREE_PATH, oldTreePath, newTreePath));
        update(updateWrapper);
    }

    /**
     * 更新状态
     *
     * @param rightRightTreePath
     */
    private void updateOffStatusByRightLikeTreePath(@NonNull String rightRightTreePath) {
        final UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        updateWrapper.likeRight(TreeEntity.TREE_PATH, rightRightTreePath);
        updateWrapper.set(TreeEntity.ENABLED, Enabled.Off.getCode());
        update(updateWrapper);
    }
}
