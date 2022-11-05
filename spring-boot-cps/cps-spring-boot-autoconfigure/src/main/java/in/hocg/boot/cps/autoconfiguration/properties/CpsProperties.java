package in.hocg.boot.cps.autoconfiguration.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@ConfigurationProperties(CpsProperties.PREFIX)
public class CpsProperties {
    public static final String PREFIX = "boot.cps";
    /**
     * 大淘客
     */
    private DaTaoKeConfig daTaoKe;
    /**
     * 服务类型
     */
    private Type type = Type.DaTaoKe;

    public enum Type {
        DaTaoKe,
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class DaTaoKeConfig {
        /**
         * 大淘客.appKey
         */
        private String appKey;
        /**
         * 大淘客.secret
         */
        private String secret;
        /**
         * 京东.联盟ID
         */
        private String jdUnionId;
    }
}
