package in.hocg.boot.mybatis.plus.extensions.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigValue;
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
    List<ConfigValue> listByScopeAndRefId(@Param("scope") String scope, @Param("refId") Long refId);

    Optional<ConfigValue> getByScopeAndRefIdAndName(@Param("scope") String scope, @Param("refId") Long refId, @Param("name") String name);
}
