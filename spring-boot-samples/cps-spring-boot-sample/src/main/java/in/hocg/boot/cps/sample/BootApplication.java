package in.hocg.boot.cps.sample;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.cps.autoconfiguration.enums.PlatformType;
import in.hocg.boot.cps.autoconfiguration.impl.CpsBervice;
import in.hocg.boot.cps.autoconfiguration.pojo.vo.PrivilegeLinkVo;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RestController
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class BootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        PrivilegeLinkVo privilegeLink;
        CpsBervice bervice = SpringContext.getBean(CpsBervice.class);
        log.info("==================================================");
        privilegeLink = bervice.getPrivilegeLink("https://detail.tmall.com/item.htm?id=638383979132&scm=1007.40986.275655.0&pvid=4381710b-90f0-4466-87e2-cf57e874b5c9").orElse(null);
        log.info("TaoBao.{}", JSONUtil.toJsonStr(privilegeLink));
        privilegeLink = bervice.getPrivilegeLink("https://item.jd.com/10035254492755.html").orElse(null);
        log.info("Jd.{}", JSONUtil.toJsonStr(privilegeLink));
        log.info("==================================================");
        privilegeLink = bervice.getPrivilegeLink(PlatformType.TaoBao, "563867605080");
        log.info("TaoBao.{}", JSONUtil.toJsonStr(privilegeLink));
        privilegeLink = bervice.getPrivilegeLink(PlatformType.Jd, "https://item.jd.com/10035254492755.html");
        log.info("Jd.{}", JSONUtil.toJsonStr(privilegeLink));
    }
}
