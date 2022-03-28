package in.hocg.boot.mybatis.plus.extensions.config.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class ConfigScopeItemVo implements Serializable {
    /**
     * 配置项
     */
    private Long valueId;
    /**
     * 配置域
     */
    private Long scopeId;
    /**
     * 配置项
     */
    private Long itemId;
    /**
     * 配置项编码
     */
    private String name;
    /**
     * 配置项标题
     */
    private String title;
    /**
     * 配置项描述
     */
    private String remark;
    /**
     * 配置项值(可能为空)
     */
    private String value;
    /**
     * 配置项值类型
     */
    private String type;
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
}
