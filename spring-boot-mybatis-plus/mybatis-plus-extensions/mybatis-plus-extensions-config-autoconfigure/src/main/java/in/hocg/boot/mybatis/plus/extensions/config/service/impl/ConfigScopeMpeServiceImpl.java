package in.hocg.boot.mybatis.plus.extensions.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.convert.UseConvert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.config.convert.ConfigMpeConvert;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.mapper.ConfigScopeMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigItemMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigScopeMpeService;
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
    private final ConfigItemMpeService configItemMpeService;

    @Override
    public Optional<ConfigScope> getByScope(String scope) {
        return lambdaQuery().eq(ConfigScope::getScope, scope).oneOpt();
    }



}
