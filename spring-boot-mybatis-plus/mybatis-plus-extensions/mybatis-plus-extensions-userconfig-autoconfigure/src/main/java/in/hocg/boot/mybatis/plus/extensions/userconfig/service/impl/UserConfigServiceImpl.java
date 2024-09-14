package in.hocg.boot.mybatis.plus.extensions.userconfig.service.impl;

import cn.hutool.core.collection.CollUtil;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.userconfig.convert.UserConfigConvert;
import in.hocg.boot.mybatis.plus.extensions.userconfig.entity.UserConfig;
import in.hocg.boot.mybatis.plus.extensions.userconfig.mapper.UserConfigMapper;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.ClearRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.DeleteRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.QueryRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.SetRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.service.UserConfigService;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.utils.struct.KeyValue;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>
 * [BOOT] 用户配置表 服务实现类
 * </p>
 *
 * @author hocgin
 * @since 2023-01-20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserConfigServiceImpl extends AbstractServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<KeyValue> set(SetRo ro) {
        Long userId = ro.getOptUserId();
        String source = ro.getSource();

        List<UserConfig> oldEntities = this.listByUserIdAndScopeAndKeys(userId, source, LangUtils.toList(ro.getList(), KeyValue::getKey));
        List<UserConfig> entities = UserConfigConvert.asUserConfig(ro);

        BiFunction<UserConfig, UserConfig, Boolean> isSame = (t1, t2) -> {
            boolean isEquals = LangUtils.equals(t1.getCode(), t2.getCode());
            if (isEquals) {
                t1.setId(t2.getId());
            }
            return isEquals;
        };
        LangUtils.getMixed(entities, oldEntities, isSame);
        this.saveOrUpdateBatch(entities);
        return UserConfigConvert.asKeyValue(entities);
    }

    private List<UserConfig> listByUserIdAndScopeAndKeys(Long userId, String scope, List<Serializable> keys) {
        return lambdaQuery().eq(UserConfig::getUserId, userId).eq(UserConfig::getScope, scope).in(UserConfig::getCode, keys).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<KeyValue> delete(DeleteRo ro) {
        List<UserConfig> entities = this.listByUserIdAndScopeAndKeys(ro.getOptUserId(), ro.getSource(), ro.getKeys());
        if (CollUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }
        this.removeBatchByIds(LangUtils.toList(entities, CommonEntity::getId));
        return UserConfigConvert.asKeyValue(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<KeyValue> clear(ClearRo ro) {
        List<UserConfig> entities = lambdaQuery().eq(UserConfig::getUserId, ro.getOptUserId()).eq(UserConfig::getScope, ro.getSource()).list();
        if (CollUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }
        this.removeBatchByIds(LangUtils.toList(entities, CommonEntity::getId));
        return UserConfigConvert.asKeyValue(entities);
    }

    @Override
    public List<KeyValue> query(QueryRo ro) {
        List<UserConfig> entities = this.listByUserIdAndScopeAndKeys(ro.getOptUserId(), ro.getSource(), ro.getKeys());
        return UserConfigConvert.asKeyValue(entities);
    }
}
