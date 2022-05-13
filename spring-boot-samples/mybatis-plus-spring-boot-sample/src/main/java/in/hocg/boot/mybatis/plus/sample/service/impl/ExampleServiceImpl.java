package in.hocg.boot.mybatis.plus.sample.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.enhance.convert.UseConvert;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import in.hocg.boot.mybatis.plus.sample.convert.ExampleConvert;
import in.hocg.boot.mybatis.plus.sample.dto.ExampleVo;
import in.hocg.boot.mybatis.plus.sample.entity.Example;
import in.hocg.boot.mybatis.plus.sample.mapper.ExampleMapper;
import in.hocg.boot.mybatis.plus.sample.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
@Slf4j
@Service
@UseConvert(ExampleConvert.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ExampleServiceImpl extends AbstractServiceImpl<ExampleMapper, Example>
    implements ExampleService {
    private final ExampleConvert convert;

    @Override
    public String index() {
        return baseMapper.index();
    }

    @Override
    public ExampleVo testAs() {
        return as(new Example(), ExampleVo.class);
    }

    @Override
    public Optional<Example> firstOne() {
        Optional<Example> result1 = this.first(new QueryWrapper<Example>().eq("id", 1));
        log.info("result1: {}", result1.orElse(null));

        Optional<Example> result2 = this.first(lambdaQuery().eq(CommonEntity::getId, 1));
        log.info("result2: {}", result2.orElse(null));
        return result2;
    }

    public ExampleVo testAs2() {
        as(new Example(), convert::asExampleVo);
        as(PageUtils.fillPage(List.of(new Example())), convert::asExampleVo);
        as(PageUtils.fillScroll(List.of(new Example()), CommonEntity::getId), convert::asExampleVo);
        as(List.of(new Example()), convert::asExampleVo);


        List<Example> examples = PageUtils.fillIndex(1L, 1L, 1L, List.of(), true, Example::getIdx);
        IPage<Example> exampleIPage = PageUtils.fillIndex(PageUtils.fillPage(List.of(new Example())).setTotal(1000L), true, Example::getIdx);
        PageUtils.fillScroll(exampleIPage, Example::getId);

        return as(new Example(), convert::asExampleVo);
    }
}
