package in.hocg.boot.sso.client.autoconfigure.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.LangUtils;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hocgin
 */
@UtilityClass
public class AuthoritiesUtils {


    /**
     * 从 attributes 属性中获取授权的列表
     *
     * @param attributes
     * @return
     */
    public List<SimpleGrantedAuthority> getAuthorities(Map<String, Object> attributes) {
        return ((List<Map<String, String>>) attributes.getOrDefault("authorities", Collections.emptyList())).stream()
            .map(item -> item.get("authority")).filter(StrUtil::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String[] asRoles(List<String> roles) {
        if (CollUtil.isEmpty(roles)) {
            return new String[]{};
        }
        return LangUtils.toList(roles, AuthoritiesUtils::removeRolePrefix).toArray(String[]::new);
    }

    public String removeRolePrefix(String role) {
        return StrUtil.removePrefix(role, "ROLE_");
    }
}
