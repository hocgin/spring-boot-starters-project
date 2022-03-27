package in.hocg.boot.mybatis.plus.extensions.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.extensions.config.convert.ConfigMpeConvert;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
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
    public void setValue(String scope, Long refId, String name, Object value) {
        Optional<ConfigItem> configItemOpt = configItemMpeService.getByScopeAndName(scope, name);
        if (configItemOpt.isEmpty()) {
            return;
        }
        ConfigItem item = configItemOpt.get();
        String inValue = ConfigHelper.toValue(ObjectUtil.defaultIfNull(value, item.getDefaultValue()));
        Assert.isTrue(!item.getNullable() && Objects.isNull(inValue), "配置项不允许为空");
        ConfigValue newConfigValue = configValueMpeService.getByItemIdAndRefId(item.getId(), refId)
            .map(configValue -> configValue.setValue(inValue))
            .orElse(new ConfigValue().setValue(inValue).setItemId(item.getId()).setRefId(refId));
        configValueMpeService.save(newConfigValue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setValue(Long valueId, Object value) {
        Optional.ofNullable(configValueMpeService.getById(valueId)).map(configValue -> {
            Long itemId = configValue.getItemId();
            ConfigItem item = configItemMpeService.getById(itemId);
            String inValue = ConfigHelper.toValue(ObjectUtil.defaultIfNull(value, item.getDefaultValue()));
            Assert.isTrue(!item.getNullable() && Objects.isNull(inValue), "配置项不允许为空");
            return configValue.setValue(inValue);
        }).ifPresent(configValueMpeService::save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setScopeStruct(String scope, String name, ScopeStructRo ro) {
        ConfigItem entity = configScopeMpeService.getByScope(scope).map(configScope -> {
            ConfigItem configItem = convert.asConfigItem(ro);
            configItem.setScopeId(configScope.getId());
            configItem.setName(name);
            return configItem;
        }).orElseGet(() -> configItemMpeService.getByScopeAndName(scope, name)
            .map(ci -> {
                ConfigItem configItem = convert.asConfigItem(ro);
                configItem.setId(ci.getId());
                configItem.setScopeId(ci.getScopeId());
                configItem.setName(ci.getName());
                return configItem;
            }).orElseThrow());
        configItemMpeService.save(entity);
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
        return configScopeMpeService.listByIds(maps.keySet()).stream().map(configScope ->
                new ConfigScopeStructVo()
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
