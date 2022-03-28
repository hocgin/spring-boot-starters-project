package in.hocg.boot.mybatis.plus.extensions.config.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigValueMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigValueMpeServiceImpl extends AbstractServiceImpl<ConfigValueMpeMapper, ConfigValue>
    implements ConfigValueMpeService {

    @Override
    public List<ConfigScopeItemVo> listConfigScopeItemVoByScopeAndRefIdAndReadable(List<String> scope, Long refId, Boolean readable) {
        if (Objects.isNull(refId)) {
            return baseMapper.listConfigScopeItemVoByScopeAndReadable(scope, readable);
        }
        return baseMapper.listConfigScopeItemVoByScopeAndRefIdAndReadable(scope, refId, readable);
    }

    @Override
    public Optional<ConfigValue> getByItemIdAndRefId(Long itemId, Long refId) {
        return lambdaQuery().eq(ConfigValue::getItemId, itemId).eq(ConfigValue::getRefId, refId).oneOpt();
    }

    @Override
    public Optional<ConfigScopeItemVo> getConfigScopeItemVoByScopeAndRefIdAndName(String scope, Long refId, String name) {
        if (Objects.isNull(refId)) {
            return baseMapper.getConfigScopeItemVoByScopeAndName(scope, name);
        }
        return baseMapper.getConfigScopeItemVoByScopeAndRefIdAndName(scope, refId, name);
    }
}
