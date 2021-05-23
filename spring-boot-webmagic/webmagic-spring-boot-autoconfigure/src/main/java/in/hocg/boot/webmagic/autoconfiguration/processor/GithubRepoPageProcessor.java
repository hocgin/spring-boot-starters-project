package in.hocg.boot.webmagic.autoconfiguration.processor;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by hocgin on 2021/5/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class GithubRepoPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(300).setTimeOut(10000);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new us.codecraft.webmagic.processor.example.GithubRepoPageProcessor())
            .addUrl("https://github.com/hocgin")
            .addPipeline(new Pipeline() {
                @Override
                public void process(ResultItems resultItems, Task task) {
                    System.out.println(resultItems);
                }
            })
            .thread(10)
            .run();
    }
}
