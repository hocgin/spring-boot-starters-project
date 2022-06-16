package in.hocg.boot.mybatis.plus.extensions.webmagic.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.extensions.webmagic.entity.Webmagic;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.webmagic.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.CreateWebmagicRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.WebmagicScrollRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.vo.WebmagicVo;

/**
 * <p>
 * [BOOT] 爬虫采集表 服务类
 * </p>
 *
 * @author hocgin
 * @since 2022-06-16
 */
public interface WebmagicService extends AbstractService<Webmagic> {

    void create(CreateWebmagicRo ro);

    WebmagicVo getLastByTypeOrderByDesc(String type, Status status);

    IScroll<WebmagicVo> scroll(WebmagicScrollRo ro);
}
