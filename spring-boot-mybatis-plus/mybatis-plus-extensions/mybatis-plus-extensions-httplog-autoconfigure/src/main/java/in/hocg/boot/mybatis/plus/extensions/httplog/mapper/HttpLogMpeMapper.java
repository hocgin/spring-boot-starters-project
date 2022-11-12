package in.hocg.boot.mybatis.plus.extensions.httplog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.httplog.entity.HttpLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface HttpLogMpeMapper extends BaseMapper<HttpLog> {
}
