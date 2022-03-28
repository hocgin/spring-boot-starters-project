package in.hocg.boot.mybatis.plus.extensions.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.config.convert.ConfigMpeConvert;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.ScopeStructRo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigItemMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigScopeMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.utils.ConfigHelper;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigMpeServiceImpl implements ConfigMpeService {
    private final ConfigScopeMpeService configScopeMpeService;
    private final ConfigItemMpeService configItemMpeService;
    private final ConfigValueMpeService configValueMpeService;
    private final ConfigMpeConvert convert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Optional<Long> setValue(String scope, Long refId, String name, Object value) {
        Optional<ConfigItem> configItemOpt = configItemMpeService.getByScopeAndName(scope, name);
        if (configItemOpt.isEmpty()) {
            return Optional.empty();
        }
        return setValue(configItemOpt.get(), refId, value);
    }

    @Override
    public Optional<Long> setValue(Long itemId, Long refId, Object value) {
        ConfigItem item = configItemMpeService.getById(itemId);
        if (Objects.isNull(item)) {
            return Optional.empty();
        }
        return setValue(item, refId, value);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setValue(Long valueId, Object value) {
        ConfigValue configValue = configValueMpeService.getById(valueId);
        if (Objects.isNull(configValue)) {
            return;
        }
        Long itemId = configValue.getItemId();
        ConfigItem item = configItemMpeService.getById(itemId);
        setValue(item, configValue.getRefId(), value);
    }

    private Optional<Long> setValue(ConfigItem item, Long refId, Object value) {
        Long itemId = item.getId();
        String inValue = ConfigHelper.toValue(ObjectUtil.defaultIfNull(value, item.getDefaultValue()));
        if (!item.getNullable()) {
            Assert.notNull(inValue, "配置项不允许为空");
        }

        ConfigValue newConfigValue = configValueMpeService.getByItemIdAndRefId(itemId, refId)
            .map(configValue -> configValue.setValue(inValue))
            .orElse(new ConfigValue().setValue(inValue).setItemId(item.getId()).setRefId(refId));
        configValueMpeService.saveOrUpdate(newConfigValue);
        return Optional.of(newConfigValue.getId());
    }

    @Override
    public Long getOrCreateScope(@NotNull String scope) {
        return configScopeMpeService.getByScope(scope).map(CommonEntity::getId)
            .orElseGet(() -> {
                ConfigScope entity = new ConfigScope();
                configScopeMpeService.saveOrUpdate(entity.setScope(scope));
                return entity.getId();
            });
    }

    @Override
    public void setScope(Long scopeId, String scope, String title, String remark) {
        ConfigScope entity = new ConfigScope();
        entity.setId(scopeId);
        entity.setScope(scope);
        entity.setTitle(title);
        entity.setRemark(remark);
        configScopeMpeService.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long setScopeStruct(String scope, String name, ScopeStructRo ro) {
        Long scopeId = getOrCreateScope(scope);
        ConfigItem entity = configItemMpeService.getByScopeIdAndName(scopeId, name)
            .map(configItem -> {
                ConfigItem result = ro.asConfigItem();
                result.setScopeId(scopeId);
                result.setName(name);
                result.setId(configItem.getId());
                return result;
            }).orElseGet(() -> {
                ConfigItem result = ro.asConfigItem();
                result.setScopeId(scopeId);
                return result.setName(name);
            });
        configItemMpeService.saveOrUpdate(entity);
        return entity.getId();
    }

    @Override
    public List<ConfigScopeStructVo> getScopeStruct(Long refId, List<String> scope, Boolean readable) {
        if (CollUtil.isEmpty(scope)) {
            return Lists.newArrayList();
        }

        List<ConfigScopeItemVo> items = configValueMpeService.listConfigScopeItemVoByScopeAndRefIdAndReadable(scope, refId, readable);
        if (CollUtil.isEmpty(items)) {
            return Lists.newArrayList();
        }

        Map<Long, List<ConfigScopeItemVo>> maps = LangUtils.toGroup(items, ConfigScopeItemVo::getScopeId);
        return configScopeMpeService.listByIds(maps.keySet()).stream()
            .map(configScope -> new ConfigScopeStructVo()
                .setTitle(configScope.getTitle())
                .setRemark(configScope.getRemark())
                .setItems(maps.getOrDefault(configScope.getId(), Collections.emptyList()))
                .setScope(configScope.getScope()))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ConfigScopeItemVo> getScopeItem(Long refId, @NotNull String scope, @NotNull String name) {
        return configValueMpeService.getConfigScopeItemVoByScopeAndRefIdAndName(scope, refId, name);
    }
}
