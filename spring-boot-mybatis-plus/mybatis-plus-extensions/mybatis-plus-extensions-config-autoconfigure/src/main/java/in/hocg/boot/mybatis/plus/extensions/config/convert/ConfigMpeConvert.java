package in.hocg.boot.mybatis.plus.extensions.config.convert;

import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import in.hocg.boot.mybatis.plus.extensions.config.pojo.ro.ScopeStructRo;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigItemMpeService;
import in.hocg.boot.mybatis.plus.extensions.config.service.ConfigValueMpeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ConfigMpeConvert {
    private final ConfigValueMpeService configValueMpeService;
    private final ConfigItemMpeService configItemMpeService;

    public ConfigItem asConfigItem(ScopeStructRo ro) {
        return new ConfigItem()
            .setType(ro.getType())
            .setTitle(ro.getTitle())
            .setDefaultValue(ro.getDefaultValue())
            .setNullable(ro.getNullable())
            .setReadable(ro.getReadable())
            .setWritable(ro.getWritable());
    }
}
