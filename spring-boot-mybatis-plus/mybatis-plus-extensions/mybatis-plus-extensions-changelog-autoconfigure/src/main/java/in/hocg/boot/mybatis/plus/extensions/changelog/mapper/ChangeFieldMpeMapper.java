package in.hocg.boot.mybatis.plus.extensions.changelog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ChangeFieldMpeMapper extends BaseMapper<ChangeField> {
}
