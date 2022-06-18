package in.hocg.boot.webmagic.autoconfiguration.core;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.webmagic.autoconfiguration.listener.event.WebmagicEvent;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class AbsAfterExtractor implements AfterExtractor {
    @Override
    public void afterProcess(Page page) {

        // 发布事件
        SpringContext.getApplicationContext().publishEvent(new WebmagicEvent(page.getUrl().get(), this));
    }
}
