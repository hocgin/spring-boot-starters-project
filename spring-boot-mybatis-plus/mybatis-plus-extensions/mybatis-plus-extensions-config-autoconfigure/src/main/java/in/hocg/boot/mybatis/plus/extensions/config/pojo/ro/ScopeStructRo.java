package in.hocg.boot.mybatis.plus.extensions.config.pojo.ro;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class ScopeStructRo implements Serializable {
    /**
     * 配置项值类型
     */
    private String type;
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
}
