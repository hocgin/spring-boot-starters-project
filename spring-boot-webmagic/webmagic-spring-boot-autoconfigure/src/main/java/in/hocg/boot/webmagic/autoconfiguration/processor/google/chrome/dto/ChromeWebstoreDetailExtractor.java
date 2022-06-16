package in.hocg.boot.webmagic.autoconfiguration.processor.google.chrome.dto;

import in.hocg.boot.webmagic.autoconfiguration.core.AbsAfterExtractor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by hocgin on 2022/6/15
 * email: hocgin@gmail.com
 * exp: https://chrome.google.com/webstore/detail/weather/ibieofighcnndjcjchdahdiacjpmkhgf
 * dow: https://clients2.google.com/service/update2/crx?response=redirect&os=win&arch=x64&os_arch=x86_64&nacl_arch=x86-64&prod=chromecrx&prodchannel=&prodversion=77.0.3865.90&lang=zh-CN&acceptformat=crx2,crx3&x=id%3Dibieofighcnndjcjchdahdiacjpmkhgf%26installsource%3Dondemand%26uc
 * https://clients2.google.com/service/update2/crx?response=redirect&os=win&arch=x64&os_arch=x86_64&nacl_arch=x86-64&prod=chromecrx&prodchannel=&prodversion=77.0.3865.90&lang=zh-CN&acceptformat=crx2,crx3&x=id%3D{appid}%26installsource%3Dondemand%26uc
 *
 * @author hocgin
 */
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@TargetUrl("https://chrome.google.com/webstore/detail/\\w+/\\w+\\?hl=zh-CN")
public class ChromeWebstoreDetailExtractor extends AbsAfterExtractor {

    /**
     * Chrome 应用地址
     */
    @ExtractByUrl(".*")
    private String url;

    /**
     * 应用标题
     */
    @ExtractBy(value = "//h1[@class='e-f-w']/html()")
    private String title;

    /**
     * 类别
     * todo 无a标签
     */
    @ExtractBy(value = "//span[@class='e-f-yb-w']/html()")
    private String category;

    /**
     * 描述内容
     */
    @ExtractBy(value = "//pre[@class='C-b-p-j-Oa']/html()")
    private String content;

    @ExtractBy("//span[@class='C-b-p-D-Xe h-C-b-p-D-md']/text()")
    private String version;

    @ExtractBy("//span[@class='C-b-p-D-Xe h-C-b-p-D-za']/text()")
    private String size;

    @ExtractBy("//span[@class='C-b-p-D-Xe h-C-b-p-D-xh-hh']/text()")
    private String lastUpdatedAt;

    @ExtractBy("//img[@class='e-f-s']/@src")
    private String logoSrc;

    @ExtractBy("//img[@class='h-A-Ce-ze-Yf']/@src")
    private List<String> previews;

}
