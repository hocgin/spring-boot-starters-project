package in.hocg.boot.mybatis.plus.extensions.config.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigValueMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<ConfigValue> listByScopeAndRefId(String scope, Long refId) {
        return baseMapper.listByScopeAndRefId(scope, refId);
    }

    @Override
    public Optional<ConfigValue> listByScopeAndRefId(String scope, Long refId, String name) {
        return baseMapper.getByScopeAndRefIdAndName(scope, refId, name);
    }
}
