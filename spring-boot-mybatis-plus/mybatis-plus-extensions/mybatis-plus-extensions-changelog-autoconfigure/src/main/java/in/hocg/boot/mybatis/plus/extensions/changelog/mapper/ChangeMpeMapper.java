package in.hocg.boot.mybatis.plus.extensions.changelog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.Change;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface ChangeMpeMapper extends BaseMapper<Change> {
    IPage<Change> scroll(@Param("ro") ChangeLogScrollRo ro, Page<Object> ofPage);
}
