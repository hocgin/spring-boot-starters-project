package in.hocg.boot.mybatis.plus.extensions.webmagic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import in.hocg.boot.mybatis.plus.extensions.webmagic.entity.Webmagic;
import in.hocg.boot.mybatis.plus.extensions.webmagic.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.webmagic.mapper.WebmagicMapper;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.CreateWebmagicRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.WebmagicScrollRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.vo.WebmagicVo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.service.WebmagicService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * [BOOT] 爬虫采集表 服务实现类
 * </p>
 *
 * @author hocgin
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebmagicServiceImpl extends AbstractServiceImpl<WebmagicMapper, Webmagic> implements WebmagicService {

    @Override
    public void create(CreateWebmagicRo ro) {
        Webmagic entity = BeanUtil.copyProperties(ro, Webmagic.class);
        this.save(entity);
    }

    @Override
    public WebmagicVo getLastByTypeOrderByDesc(String type, Status status) {
        LambdaQueryChainWrapper<Webmagic> wrapper = lambdaQuery().eq(Objects.nonNull(type), Webmagic::getType, type)
            .eq(Objects.nonNull(status), Webmagic::getStatus, status.getCodeStr())
            .orderByDesc(CommonEntity::getCreatedAt);
        return asWebmagicVo(this.first(wrapper).orElse(null));
    }

    @Override
    public IScroll<WebmagicVo> scroll(WebmagicScrollRo ro) {
        LambdaQueryChainWrapper<Webmagic> wrapper = lambdaQuery().eq(Objects.nonNull(ro.getStatus()), Webmagic::getStatus, ro.getStatus())
            .eq(Objects.nonNull(ro.getType()), Webmagic::getType, ro.getType())
            .orderBy(true, ro.getAsc(), CommonEntity::getCreatedAt);
        if (ro.getAsc()) {
            wrapper.gt(Objects.nonNull(ro.getNextId()), Webmagic::getId, ro.getNextId());
        } else {
            wrapper.lt(Objects.nonNull(ro.getNextId()), Webmagic::getId, ro.getNextId());
        }

        Page<Webmagic> result = wrapper.page(ro.ofPage());
        return PageUtils.fillScroll(result, Webmagic::getId).convert(this::asWebmagicVo);
    }


    private WebmagicVo asWebmagicVo(Webmagic entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return BeanUtil.copyProperties(entity, WebmagicVo.class);
    }
}
