package in.hocg.boot.mybatis.plus.extensions.config.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class ConfigScopeStructVo implements Serializable {
    /**
     * 域名称
     */
    private String title;
    /**
     * 域编号
     */
    private String scope;
    /**
     * 描述信息
     */
    private String remark;

    @Data
    public static class ItemVo {
        /**
         * 配置项编码
         */
        private String name;
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
}
