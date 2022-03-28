package in.hocg.boot;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.extensions.changelog.ChangeLogMpeService;
import in.hocg.boot.mybatis.plus.extensions.changelog.enums.ChangeType;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;
import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigScope;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigScopeMpeService;
import in.hocg.boot.mybatis.plus.extensions.sample.BootApplication;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChangeLogTests extends AbstractSpringBootTest {
    @Autowired
    ChangeLogMpeService changeLogMpeService;
    @Autowired
    ConfigScopeMpeService configScopeMpeService;

    @Test
    public void changeLog() {
        // 新增
        String test = "test1" + System.currentTimeMillis();
        ConfigScope entity = new ConfigScope();
        entity.setScope(test);
        entity = changeLogMpeService.record(ChangeType.Insert, test, entity);

        System.out.println(entity);

        // 更新
        entity.setTitle("test2");
        entity = changeLogMpeService.record(ChangeType.Modify, test, entity);

        // 删除
        entity = changeLogMpeService.record(ChangeType.Delete, test, entity);
        System.out.println("------------------");

        IScroll<ChangeVo> scroll = changeLogMpeService.scroll(new ChangeLogScrollRo());
        log.info("{}", JSONUtil.toJsonPrettyStr(scroll));
    }
}
