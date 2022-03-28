package in.hocg.boot.mybatis.plus.extensions.config.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;

import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ConfigItemMpeService extends AbstractService<ConfigItem> {
    Optional<ConfigItem> getByScopeAndName(String scope, String name);

    Optional<ConfigItem> getByScopeIdAndName(Long scopeId, String name);
}
