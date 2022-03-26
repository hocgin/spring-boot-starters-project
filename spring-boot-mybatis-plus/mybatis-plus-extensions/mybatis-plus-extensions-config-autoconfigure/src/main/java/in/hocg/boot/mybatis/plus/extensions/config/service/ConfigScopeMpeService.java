package in.hocg.boot.mybatis.plus.extensions.config.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;

import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ConfigScopeMpeService extends AbstractService<ConfigScope> {

    Optional<ConfigScope> getByScope(String scope);


}
