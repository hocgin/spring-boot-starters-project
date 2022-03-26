package in.hocg.boot.mybatis.plus.extensions.tpl.service.impl;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractServiceImpl;
import in.hocg.boot.mybatis.plus.extensions.tpl.mapper.TplMpeMapper;
import in.hocg.boot.mybatis.plus.extensions.tpl.service.TplMpeService;
import in.hocg.boot.mybatis.plus.extensions.tpl.entity.Tpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TplMpeServiceImpl extends AbstractServiceImpl<TplMpeMapper, Tpl>
    implements TplMpeService {
}
