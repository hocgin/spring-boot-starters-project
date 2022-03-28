package in.hocg.boot.mybatis.plus.extensions.config.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigItemMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigItemMpeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigItemMpeServiceImpl extends AbstractServiceImpl<ConfigItemMpeMapper, ConfigItem>
    implements ConfigItemMpeService {

    @Override
    public Optional<ConfigItem> getByScopeAndName(String scope, String name) {
        return baseMapper.getByScopeAndName(scope, name);
    }

    @Override
    public Optional<ConfigItem> getByScopeIdAndName(Long scopeId, String name) {
        return lambdaQuery().eq(ConfigItem::getScopeId, scopeId).eq(ConfigItem::getName, name).oneOpt();
    }

}
