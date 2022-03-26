package in.hocg.boot.mybatis.plus.extensions.config.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.QueryScopeRo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeValueVo;

import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ConfigScopeMpeService extends AbstractService<ConfigScope> {

    Optional<ConfigScope> getByScope(String scope);

    List<ConfigScope> listByScope(String... scopes);

    Optional<ConfigScopeStructVo> getConfigStruct(String scope);

    List<ConfigScopeStructVo> listConfigStruct(String... scopes);

    Optional<ConfigScopeValueVo> getConfig(QueryScopeRo ro);

    List<ConfigScopeValueVo> listConfig(List<QueryScopeRo> ro);
}
