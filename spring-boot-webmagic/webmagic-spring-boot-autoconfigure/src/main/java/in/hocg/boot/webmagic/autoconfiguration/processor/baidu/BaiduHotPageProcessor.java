package in.hocg.boot.webmagic.autoconfiguration.processor.baidu;

import in.hocg.boot.webmagic.autoconfiguration.core.AbsPageProcessor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2021/5/22
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class BaiduHotPageProcessor extends AbsPageProcessor {
    public static final String RESULT = "result";

    @Data
    @Accessors(chain = true)
    public static class Row {
        private String imgSrc;
        private String rank;
        private String title;
        private String desc;
        private String href;
        private String hotDesc;
        private String hotNumber;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        List<Selectable> nodes = html.xpath("//*[@id='sanRoot']/main/div[2]/div/div[2]/*").nodes();
        BaiduHotPageProcessor.Row row;
        List<BaiduHotPageProcessor.Row> result = new ArrayList<>(nodes.size());
        int i = 0;
        for (Selectable node : nodes) {
            row = new Row();
            row.setRank(String.valueOf(++i));
            row.setHotNumber(node.xpath("//div[1]/div[2]/text()").get());
            row.setHref(node.xpath("//a/@href").get());
            row.setImgSrc(node.xpath("//a/img/@src").get());
            row.setTitle(node.xpath("//div[2]/a/text()").get());
            row.setHotDesc(node.xpath("//div[2]/a/div/text()").get());
            row.setDesc(node.xpath("//div[2]/div[1]/tidyText()").get());
            result.add(row);
        }
        page.putField(RESULT, result);
    }

    @Getter
    @RequiredArgsConstructor
    enum URL {
        Realtime("https://top.baidu.com/board?tab=realtime", "热点"),
        Novel("https://top.baidu.com/board?tab=novel", "小说"),
        Movie("https://top.baidu.com/board?tab=movie", "电影"),
        Teleplay("https://top.baidu.com/board?tab=teleplay", "电视剧"),
        Cartoon("https://top.baidu.com/board?tab=cartoon", "动漫"),
        Variety("https://top.baidu.com/board?tab=variety", "综艺"),
        Documentary("https://top.baidu.com/board?tab=documentary", "纪录片"),
        Star("https://top.baidu.com/board?tab=star", "明星"),
        Car("https://top.baidu.com/board?tab=car", "汽车"),
        Game("https://top.baidu.com/board?tab=game", "游戏"),
        ;
        private final String url;
        private final String desc;
    }


    public static void main(String[] args) {
        Spider.create(new BaiduHotPageProcessor())
            .addUrl(URL.Realtime.getUrl())
            .addPipeline((resultItems, task) -> {
                List<BaiduHotPageProcessor.Row> rankRows = resultItems.get(RESULT);
                System.out.println(rankRows);
            })
            .run();
    }
}
