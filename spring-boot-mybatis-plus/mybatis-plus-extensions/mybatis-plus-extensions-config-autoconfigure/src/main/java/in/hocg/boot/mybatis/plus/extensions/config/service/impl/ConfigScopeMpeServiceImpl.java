package in.hocg.boot.mybatis.plus.extensions.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.convert.UseConvert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.config.convert.ConfigMpeConvert;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigScopeMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.QueryScopeRo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeValueVo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigScopeMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@UseConvert(ConfigMpeConvert.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigScopeMpeServiceImpl extends AbstractServiceImpl<ConfigScopeMpeMapper, ConfigScope>
    implements ConfigScopeMpeService {
    private final ConfigMpeConvert convert;

    @Override
    public Optional<ConfigScope> getByScope(String scope) {
        return lambdaQuery().eq(ConfigScope::getScope, scope).oneOpt();
    }

    @Override
    public List<ConfigScope> listByScope(String... scopes) {
        if (ArrayUtil.isEmpty(scopes)) {
            return CollUtil.newArrayList();
        }
        return lambdaQuery().in(ConfigScope::getScope, Lists.newArrayList(scopes)).list();
    }

    @Override
    public Optional<ConfigScopeStructVo> getConfigStruct(String scope) {
        return getByScope(scope).map(entity -> as(entity, ConfigScopeStructVo.class));
    }

    @Override
    public List<ConfigScopeStructVo> listConfigStruct(String... scopes) {
        if (ArrayUtil.isEmpty(scopes)) {
            return CollUtil.newArrayList();
        }
        return this.listByScope(scopes).stream().map(entity -> as(entity, ConfigScopeStructVo.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<ConfigScopeValueVo> getConfig(QueryScopeRo ro) {
        if (Objects.isNull(ro)) {
            return Optional.empty();
        }
        String scope = ro.getScope();
        return getByScope(scope).map(entity -> convert.asConfigScopeValueVo(entity, ro.getRefId()));
    }

    @Override
    public List<ConfigScopeValueVo> listConfig(List<QueryScopeRo> ro) {
        return ro.stream().flatMap(queryScopeRo -> getConfig(queryScopeRo).stream()).collect(Collectors.toList());
    }
}
