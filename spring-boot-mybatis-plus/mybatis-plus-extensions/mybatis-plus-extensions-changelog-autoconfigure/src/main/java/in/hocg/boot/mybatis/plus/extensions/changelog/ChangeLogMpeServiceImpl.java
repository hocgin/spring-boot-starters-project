package in.hocg.boot.mybatis.plus.extensions.changelog;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.extensions.changelog.convert.ChangeConvert;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.ChangeLogDto;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.Change;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;
import in.hocg.boot.mybatis.plus.extensions.changelog.service.ChangeFieldMpeService;
import in.hocg.boot.mybatis.plus.extensions.changelog.service.ChangeMpeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ChangeLogMpeServiceImpl implements ChangeLogMpeService {
    private final ChangeMpeService changeMpeService;
    private final ChangeFieldMpeService changeFieldMpeService;

    @Override
    public void record(ChangeLogDto dto) {
        Change entity = ChangeConvert.asChange(dto);
        entity.setLogSn(IdUtil.fastSimpleUUID());
        changeMpeService.save(entity);
        List<ChangeField> changeFields = dto.getChange().stream()
            .map(fieldChangeDto -> ChangeConvert.asChangeField(fieldChangeDto).setChangeId(entity.getId()))
            .collect(Collectors.toList());
        changeFieldMpeService.saveBatch(changeFields);
    }

    @Override
    public IScroll<ChangeVo> scroll(ChangeLogScrollRo ro) {
        return changeMpeService.scroll(ro);
    }
}
