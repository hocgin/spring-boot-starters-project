package in.hocg.boot.cps.autoconfiguration.impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.cps.autoconfiguration.enums.PlatformType;
import in.hocg.boot.cps.autoconfiguration.pojo.vo.PrivilegeLinkVo;
import in.hocg.boot.utils.LangUtils;

import java.util.Map;
import java.util.Optional;

public interface CpsBervice {

    PrivilegeLinkVo getPrivilegeLink(PlatformType type, String goodId);

    default Optional<PrivilegeLinkVo> getPrivilegeLink(String url) {
        PrivilegeLinkVo result = null;
        if (StrUtil.contains(url, "taobao.com") || StrUtil.contains(url, "tmall.com")) {
            Pair<String, Map<String, String>> urlParams = LangUtils.getParams(url);
            Map<String, String> params = urlParams.getValue();
            String id = StrUtil.blankToDefault(params.get("id"), params.get("x-itemid"));
            result = getPrivilegeLink(PlatformType.TaoBao, id);
        } else if (StrUtil.contains(url, "jd.com")) {
            result = getPrivilegeLink(PlatformType.Jd, url);
        }
        return Optional.ofNullable(result);
    }

}
