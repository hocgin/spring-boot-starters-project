package in.hocg.boot.mybatis.plus.extensions.config.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;

import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ConfigValueMpeService extends AbstractService<ConfigValue> {

    List<ConfigScopeItemVo> listConfigScopeItemVoByScopeAndRefIdAndReadable(List<String> scope, Long refId, Boolean readable);

    Optional<ConfigValue> getByItemIdAndRefId(Long itemId, Long refId);

    Optional<ConfigScopeItemVo> getConfigScopeItemVoByScopeAndRefIdAndName(String scope, Long refId, String name);
}
