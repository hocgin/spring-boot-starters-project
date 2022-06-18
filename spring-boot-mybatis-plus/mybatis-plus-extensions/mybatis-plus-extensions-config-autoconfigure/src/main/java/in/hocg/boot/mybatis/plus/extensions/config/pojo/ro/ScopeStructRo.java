package in.hocg.boot.mybatis.plus.extensions.config.pojo.ro;

import in.hocg.boot.mybatis.plus.extensions.config.entity.ConfigItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
public class ScopeStructRo implements Serializable {
    /**
     * 配置项值类型
     */
    private String type = String.class.getName();
    /**
     * 配置项标题
     */
    private String title;
    /**
     * 配置项值, 默认值
     */
    private String defaultValue;
    /**
     * 是否可读
     */
    private Boolean readable;
    /**
     * 是否可写
     */
    private Boolean writable;
    /**
     * 是否可空
     */
    private Boolean nullable;
    /**
     * 备注
     */
    private String remark;

    public ConfigItem asConfigItem() {
        return new ConfigItem()
            .setType(this.getType())
            .setTitle(this.getTitle())
            .setRemark(this.getRemark())
            .setDefaultValue(this.getDefaultValue())
            .setWritable(this.getWritable())
            .setNullable(this.getNullable())
            .setReadable(this.getReadable());
    }
}
