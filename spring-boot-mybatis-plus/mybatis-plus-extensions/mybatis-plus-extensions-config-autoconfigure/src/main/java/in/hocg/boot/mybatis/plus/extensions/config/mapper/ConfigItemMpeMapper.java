package in.hocg.boot.mybatis.plus.extensions.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ConfigItemMpeMapper extends BaseMapper<ConfigItem> {
    List<ConfigItem> listByScope(@Param("scope") String scope);

    Optional<ConfigItem> getByScopeAndName(@Param("scope") String scope, @Param("name") String name);

}
