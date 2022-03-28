package in.hocg.boot.mybatis.plus.extensions.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ConfigValueMpeMapper extends BaseMapper<ConfigValue> {

    List<ConfigScopeItemVo> listConfigScopeItemVoByScopeAndRefIdAndReadable(@Param("scope") List<String> scope, @Param("refId") Long refId, @Param("readable") Boolean readable);

    List<ConfigScopeItemVo> listConfigScopeItemVoByScopeAndReadable(@Param("scope") List<String> scope, @Param("readable") Boolean readable);

    Optional<ConfigScopeItemVo> getConfigScopeItemVoByScopeAndRefIdAndName(@Param("scope") String scope, @Param("refId") Long refId, @Param("name") String name);

    Optional<ConfigScopeItemVo> getConfigScopeItemVoByScopeAndName(@Param("scope") String scope, @Param("name") String name);
}
