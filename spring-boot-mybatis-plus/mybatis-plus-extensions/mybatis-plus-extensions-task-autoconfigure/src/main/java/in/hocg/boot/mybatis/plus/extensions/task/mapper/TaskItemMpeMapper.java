package in.hocg.boot.mybatis.plus.extensions.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Mapper
public interface TaskItemMpeMapper extends BaseMapper<TaskItem> {
}
