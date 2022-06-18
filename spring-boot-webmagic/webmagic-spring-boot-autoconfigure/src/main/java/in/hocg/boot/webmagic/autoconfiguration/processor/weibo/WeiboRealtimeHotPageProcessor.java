package in.hocg.boot.webmagic.autoconfiguration.processor.weibo;

import in.hocg.boot.webmagic.autoconfiguration.core.AbsPageProcessor;
import lombok.Data;
import lombok.experimental.Accessors;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2021/5/22
 * email: hocgin@gmail.com
 * 微博热搜
 *
 * @author hocgin
 */
public class WeiboRealtimeHotPageProcessor extends AbsPageProcessor {
    public static final String RESULT = "result";

    @Data
    @Accessors(chain = true)
    public static class RankRow {
        private Boolean isTop = false;
        private String rank;
        private String title;
        private String href;
        private String hotDesc;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//*[@id='pl_top_realtimehot']/table/tbody/tr").nodes();
        RankRow rankRow;
        List<RankRow> result = new ArrayList<>(nodes.size());
        for (Selectable node : nodes) {
            rankRow = new RankRow();
            rankRow.setIsTop(Objects.nonNull(node.xpath("//td/i[@class='icon-top']").get()));
            rankRow.setRank(node.xpath("//td[@class='ranktop']/tidyText()").get());
            rankRow.setTitle(node.xpath("//td[2]/a/text()").get());
            rankRow.setHref(node.xpath("//td[2]/a/@href").get());
            rankRow.setHotDesc(node.xpath("//td[3]/i/text()").get());

            result.add(rankRow);
        }
        page.putField(RESULT, result);
    }

    public static void main(String[] args) {
        Spider.create(new WeiboRealtimeHotPageProcessor())
            .addUrl("https://s.weibo.com/top/summary?cate=realtimehot")
            .addPipeline((resultItems, task) -> {
                List<RankRow> rankRows = resultItems.get(RESULT);
                System.out.println(rankRows);
            })
            .run();
    }

}
