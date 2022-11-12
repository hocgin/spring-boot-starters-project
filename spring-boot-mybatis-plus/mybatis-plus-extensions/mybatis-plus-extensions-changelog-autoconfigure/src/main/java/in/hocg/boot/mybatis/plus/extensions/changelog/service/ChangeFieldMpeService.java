package in.hocg.boot.mybatis.plus.extensions.changelog.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.changelog.entity.ChangeField;

import java.util.List;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ChangeFieldMpeService extends AbstractService<ChangeField> {
    List<ChangeField> listByChangeId(List<Long> changeIds);
}
