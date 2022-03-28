package in.hocg.boot.mybatis.plus.extensions.task.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskItemLog;
import in.hocg.boot.mybatis.plus.extensions.task.mapper.TaskItemLogMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskItemLogMpeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaskItemLogMpeServiceImpl extends AbstractServiceImpl<TaskItemLogMpeMapper, TaskItemLog>
    implements TaskItemLogMpeService {
}
