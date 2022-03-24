package in.hocg.boot.mybatis.plus.extensions.config.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ConfigScopeMpeMapper extends BaseMapper<ConfigScope> {
}
