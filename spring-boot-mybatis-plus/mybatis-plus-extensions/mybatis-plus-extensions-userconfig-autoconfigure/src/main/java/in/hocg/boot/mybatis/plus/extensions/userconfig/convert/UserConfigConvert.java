package in.hocg.boot.mybatis.plus.extensions.userconfig.convert;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.mybatis.plus.extensions.userconfig.entity.UserConfig;
import in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro.SetRo;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.utils.struct.KeyValue;

import java.util.List;

/**
 * Created by hocgin on 2023/01/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class UserConfigConvert {
    public static List<UserConfig> asUserConfig(SetRo ro) {
        Long optUserId = ro.getOptUserId();
        String source = ro.getSource();
        return LangUtils.toList(ro.getList(), item -> UserConfigConvert.asUserConfig(item, optUserId, source));
    }

    private static UserConfig asUserConfig(KeyValue ro, Long optUserId, String source) {
        UserConfig entity = new UserConfig();
        entity.setCode((String) ro.getKey());
        entity.setValue(StrUtil.toString(ro.getValue()));
        entity.setUserId(optUserId);
        entity.setScope(source);
        return entity;
    }

    public static List<KeyValue> asKeyValue(List<UserConfig> entities) {
        return LangUtils.toList(entities, UserConfigConvert::asKeyValue);
    }

    private static KeyValue asKeyValue(UserConfig entity) {
        KeyValue result = new KeyValue();
        result.setValue(entity.getValue());
        result.setKey(entity.getCode());
        return result;
    }
}
