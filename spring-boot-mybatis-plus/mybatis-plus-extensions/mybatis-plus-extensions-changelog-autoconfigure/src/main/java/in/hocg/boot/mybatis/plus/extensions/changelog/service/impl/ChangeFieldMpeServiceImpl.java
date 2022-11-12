package in.hocg.boot.mybatis.plus.extensions.changelog.service.impl;

import cn.hutool.core.collection.CollUtil;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;
import in.hocg.boot.mybatis.plus.extensions.changelog.mapper.ChangeFieldMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.changelog.service.ChangeFieldMpeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ChangeFieldMpeServiceImpl extends AbstractServiceImpl<ChangeFieldMpeMapper, ChangeField>
    implements ChangeFieldMpeService {
    @Override
    public List<ChangeField> listByChangeId(List<Long> changeIds) {
        if (CollUtil.isEmpty(changeIds)) {
            return CollUtil.newArrayList();
        }
        return lambdaQuery().in(ChangeField::getChangeId, changeIds).list();
    }
}
