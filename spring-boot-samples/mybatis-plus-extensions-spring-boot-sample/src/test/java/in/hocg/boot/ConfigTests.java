package in.hocg.boot;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.mybatis.plus.extensions.config.ConfigMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.ScopeStructRo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeItemVo;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.vo.ConfigScopeStructVo;
import in.hocg.boot.mybatis.plus.extensions.sample.BootApplication;
import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigTests extends AbstractSpringBootTest {
    @Autowired(required = false)
    ConfigMpeService configMpeService;

    @Test
    public void create() {
        String scope = "user";

        ScopeStructRo ro = new ScopeStructRo();
        ro.setType(String.class.getName());
        ro.setTitle("用户名");
        ro.setNullable(false);
        Long itemId = configMpeService.setScopeStruct(scope, "username", ro);
        log.info("itemId: {}", itemId);

        configMpeService.setValue(itemId, 1L, "hocgin");
        configMpeService.setValue(itemId, 2L, "hocgin2");


        ConfigScopeStructVo scopeStruct = configMpeService.getScopeStruct(scope).orElse(null);
        log.info("查单个域, 只查结构:\n {}", JSONUtil.toJsonPrettyStr(scopeStruct));

        List<String> groups = List.of(scope);
        List<ConfigScopeStructVo> result = configMpeService.getScopeStruct(groups);
        log.info("查多个域, 只查结构:\n {}", JSONUtil.toJsonPrettyStr(result));

        List<ConfigScopeStructVo> result2 = configMpeService.getScopeStruct(1L, groups);
        log.info("查多个域, 含值:\n {}", JSONUtil.toJsonPrettyStr(result2));

        ConfigScopeItemVo item = configMpeService.getScopeItem(scope, "username").orElse(null);
        log.info("查域项, 只查结构:\n {}", JSONUtil.toJsonPrettyStr(item));

        ConfigScopeItemVo item2 = configMpeService.getScopeItem(1L, scope, "username").orElse(null);
        log.info("查域项, 含值:\n {}", JSONUtil.toJsonPrettyStr(item2));

        String username = (String) configMpeService.getValue(1L, scope, "username").orElse(null);
        log.debug("查值: {}", username);
    }
}
