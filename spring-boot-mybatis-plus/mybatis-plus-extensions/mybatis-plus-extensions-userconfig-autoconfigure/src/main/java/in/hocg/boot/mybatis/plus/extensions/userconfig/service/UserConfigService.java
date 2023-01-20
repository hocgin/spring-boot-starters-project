package in.hocg.boot.mybatis.plus.extensions.userconfig.service;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.AbstractService;
import in.hocg.boot.mybatis.plus.extensions.userconfig.entity.UserConfig;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.ClearRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.DeleteRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.QueryRo;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.SetRo;
import in.hocg.boot.utils.struct.KeyValue;

import java.util.List;

/**
 * <p>
 * [BOOT] 用户配置表 服务类
 * </p>
 *
 * @author hocgin
 * @since 2023-01-20
 */
public interface UserConfigService extends AbstractService<UserConfig> {

    List<KeyValue> set(SetRo ro);

    List<KeyValue> delete(DeleteRo ro);

    List<KeyValue> clear(ClearRo ro);

    List<KeyValue> query(QueryRo ro);
}
