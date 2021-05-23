package in.hocg.boot.webmagic.autoconfiguration.processor;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by hocgin on 2021/5/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class AbsPageProcessor implements PageProcessor {
    private final Site site = Site.me().setRetryTimes(3).setSleepTime(800).setTimeOut(10000);

    @Override
    public Site getSite() {
        return site;
    }
}
