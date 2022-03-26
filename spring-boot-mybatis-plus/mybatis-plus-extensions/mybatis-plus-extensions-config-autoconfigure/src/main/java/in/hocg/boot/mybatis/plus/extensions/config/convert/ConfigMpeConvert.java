package in.hocg.boot.mybatis.plus.extensions.config.convert;

import cn.hutool.core.bean.BeanUtil;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeValueVo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigItemMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigMpeConvert {
    private final ConfigValueMpeService configValueMpeService;
    private final ConfigItemMpeService configItemMpeService;

    public ConfigScopeStructVo asConfigScopeStructVo(ConfigScope entity) {
        String scope = entity.getScope();
        ConfigScopeStructVo result = new ConfigScopeStructVo();
        result.setScope(scope);
        result.setTitle(entity.getTitle());
        result.setRemark(entity.getRemark());
        List<ConfigItem> list = configItemMpeService.listByScope(scope);
        result.setItems(list.stream().map(configItem -> BeanUtil.copyProperties(configItem, ConfigScopeItemVo.class)).collect(Collectors.toList()));
        return result;
    }

    public ConfigScopeValueVo asConfigScopeValueVo(ConfigScope entity, Long refId) {
        String scope = entity.getScope();
        ConfigScopeValueVo result = new ConfigScopeValueVo();
        result.setScope(scope);
        result.setTitle(entity.getTitle());
        result.setRemark(entity.getRemark());
        result.setItems(configValueMpeService.listConfigScopeItemVoByScopeAndRefId(scope, refId));
        return result;
    }
}
