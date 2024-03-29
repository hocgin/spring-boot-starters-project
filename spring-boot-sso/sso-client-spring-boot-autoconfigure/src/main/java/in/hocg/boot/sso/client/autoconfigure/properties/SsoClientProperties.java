package in.hocg.boot.sso.client.autoconfigure.properties;


import com.google.common.collect.Maps;
import in.hocg.boot.sso.client.autoconfigure.core.PageConstants;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 * 拦截规则优先级(高到低):
 *
 * denyUrls[黑名单] > authenticatedUrls[必须登陆] > ignoreUrls[忽略路径] > hasIpAddress[IP白名单] > hasAnyAuthority[必须权限] > hasAnyRole[必须角色]
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = SsoClientProperties.PREFIX)
public class SsoClientProperties {
    public static final String PREFIX = "boot.sso.client";
    private String logoutUrl = PageConstants.LOGOUT_URL;
    private String indexUrl = PageConstants.LOGIN_URL;
    /**
     * 忽略的路径(支持表达式)
     */
    private List<String> ignoreUrls = Collections.emptyList();
    /**
     * 拒绝访问(支持表达式)
     */
    private List<String> denyUrls = Collections.emptyList();
    /**
     * 需认证(支持表达式)
     */
    private List<String> authenticatedUrls = Collections.emptyList();
    /**
     * 需要任意角色(支持表达式)
     */
    private Map<String, List<String>> hasAnyRole = Maps.newHashMap();
    /**
     * 需要任意权限(支持表达式)
     */
    private Map<String, List<String>> hasAnyAuthority = Maps.newHashMap();
    /**
     * IP 白名单(支持表达式)
     */
    private Map<String, List<String>> hasIpAddress = Maps.newHashMap();
}
