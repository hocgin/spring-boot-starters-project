package in.hocg.boot.mybatis.plus.extensions.task.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.task.entity.TaskInfo;
import in.hocg.boot.mybatis.plus.extensions.task.mapper.TaskInfoMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.task.service.TaskInfoMpeService;
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
public class TaskInfoMpeServiceImpl extends AbstractServiceImpl<TaskInfoMpeMapper, TaskInfo>
    implements TaskInfoMpeService {
}
