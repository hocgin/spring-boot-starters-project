package in.hocg.boot.mybatis.plus.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.sample.entity.Example;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
@Mapper
public interface ExampleMapper extends BaseMapper<Example> {


    String index();

}
