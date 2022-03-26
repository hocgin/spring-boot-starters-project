package in.hocg.boot.mybatis.plus.sample.web.sample.data.none;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by hocgin on 2021/12/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface NoneModelEntityMapper extends BaseMapper<NoneModelEntity> {
    @Select("select * from none_model_entity where id = #{id}")
    NoneModelEntity byId(Long id);
}
