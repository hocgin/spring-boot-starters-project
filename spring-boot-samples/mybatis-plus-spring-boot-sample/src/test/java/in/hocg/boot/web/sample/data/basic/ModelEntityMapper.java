package in.hocg.boot.web.sample.data.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by hocgin on 2021/12/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ModelEntityMapper extends BaseMapper<ModelEntity> {
    @Select("select * from model_entity where id = #{id}")
    ModelEntity byId(Long id);

    @Update("UPDATE model_entity SET deleted_at = NULL where id = #{id}")
    Integer resetDelete(Long id);
}
