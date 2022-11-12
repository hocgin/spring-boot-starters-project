package in.hocg.boot.mybatis.plus.extensions.changelog.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.Change;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro.ChangeLogScrollRo;
import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo.ChangeVo;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ChangeMpeService extends AbstractService<Change> {
    IScroll<ChangeVo> scroll(ChangeLogScrollRo ro);
}
