package in.hocg.boot.mybatis.plus.sample.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.sample.entity.Example;
import in.hocg.boot.mybatis.plus.sample.mapper.ExampleMapper;
import in.hocg.boot.mybatis.plus.sample.service.ExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ExampleServiceImpl extends AbstractServiceImpl<ExampleMapper, Example>
    implements ExampleService {

    @Override
    public String index() {
        return baseMapper.index();
    }
}
