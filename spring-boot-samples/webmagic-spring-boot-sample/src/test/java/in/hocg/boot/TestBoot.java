package in.hocg.boot;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.extensions.webmagic.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.WebmagicScrollRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.vo.WebmagicVo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.service.WebmagicService;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.webmagic.autoconfiguration.processor.google.chrome.dto.ChromeWebstoreDetailExtractor;
import in.hocg.boot.webmagic.sample.BootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;

/**
 * Created by hocgin on 2022/6/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestBoot {

    @Test
    public void testChromeWebstoreDetail() {
        OOSpider.create(Site.me().setSleepTime(1000), new ConsolePageModelPipeline(), ChromeWebstoreDetailExtractor.class)
            .addPipeline((resultItems, task) -> {
                log.debug("resultItems: {}", resultItems);
            })
            .addUrl("https://chrome.google.com/webstore/detail/weather/ibieofighcnndjcjchdahdiacjpmkhgf?hl=zh-CN")
            .thread(5).run();
    }

    @Test
    public void testQuery() {
        WebmagicVo lastItem = SpringContext.getBean(WebmagicService.class).getLastByTypeOrderByDesc(null, Status.Done);
        Assertions.assertNotNull(lastItem);
    }

    @Test
    public void testScroll() {
        WebmagicScrollRo ro = new WebmagicScrollRo();
        IScroll<WebmagicVo> result = SpringContext.getBean(WebmagicService.class).scroll(ro);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testGithubRepo() {
        Spider.create(new us.codecraft.webmagic.processor.example.GithubRepoPageProcessor())
            .addUrl("https://github.com/hocgin")
            .addPipeline((resultItems, task) -> {
                log.debug("resultItems: {}", resultItems);
            })
            .thread(10)
            .run();
    }
}
