package in.hocg.boot.mybatis.plus.extensions.changelog.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import in.hocg.boot.mybatis.plus.extensions.changelog.convert.ChangeConvert;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.Change;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;
import in.hocg.boot.mybatis.plus.extensions.changelog.mapper.ChangeMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;
import in.hocg.boot.mybatis.plus.extensions.changelog.service.ChangeFieldMpeService;
import in.hocg.boot.mybatis.plus.extensions.changelog.service.ChangeMpeService;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ChangeMpeServiceImpl extends AbstractServiceImpl<ChangeMpeMapper, Change>
    implements ChangeMpeService {
    private final ChangeMpeMapper mapper;
    private final ChangeFieldMpeService changeFieldMpeService;

    @Override
    public IScroll<ChangeVo> scroll(ChangeLogScrollRo ro) {
        IPage<Change> scrollResult = mapper.scroll(ro, ro.ofPage());
        List<Long> changeIds = LangUtils.toList(scrollResult.getRecords(), CommonEntity::getId);
        if (CollUtil.isEmpty(changeIds)) {
            return PageUtils.emptyScroll();
        }
        Map<Long, List<ChangeField>> maps = LangUtils.toGroup(changeFieldMpeService.listByChangeId(changeIds), ChangeField::getChangeId);
        return PageUtils.fillScroll(scrollResult, Change::getId).convert(change -> ChangeConvert.asChangeVo(change, maps));
    }
}
