package in.hocg.boot;

import in.hocg.boot.webmagic.autoconfiguration.processor.google.chrome.dto.ChromeWebstoreDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
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
public class TestBoot {

    @Test
    public void testChromeWebstoreDetail() {
        OOSpider.create(Site.me().setSleepTime(1000), new ConsolePageModelPipeline(), ChromeWebstoreDetail.class)
            .addPipeline((resultItems, task) -> {
                log.debug("resultItems: {}", resultItems);
            })
            .addUrl("https://chrome.google.com/webstore/detail/weather/ibieofighcnndjcjchdahdiacjpmkhgf?hl=zh-CN")
            .thread(5).run();
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
