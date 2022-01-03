package in.hocg.boot.mybatis.plus.sample.service;


import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.sample.dto.ExampleVo;
import in.hocg.boot.mybatis.plus.sample.entity.Example;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
public interface ExampleService extends AbstractService<Example> {

    String index();

    ExampleVo testAs();
}
