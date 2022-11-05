package in.hocg.boot.cps.autoconfiguration.impl.dataoke;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import in.hocg.boot.cps.autoconfiguration.enums.PlatformType;
import in.hocg.boot.cps.autoconfiguration.impl.CpsBervice;
import in.hocg.boot.cps.autoconfiguration.impl.dataoke.lib.ApiClient;
import in.hocg.boot.cps.autoconfiguration.pojo.vo.PrivilegeLinkVo;
import in.hocg.boot.cps.autoconfiguration.properties.CpsProperties;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import java.util.TreeMap;

/**
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class DaTaoKeBerviceImpl implements CpsBervice {
    private final CpsProperties.DaTaoKeConfig config;

    @Override
    public PrivilegeLinkVo getPrivilegeLink(PlatformType type, String goodId) {
        if (PlatformType.TaoBao.anyMatch(type.getCode())) {
            return getPrivilegeLinkByTaoBao(goodId);
        } else if (PlatformType.Jd.anyMatch(type.getCode())) {
            return getPrivilegeLinkByJd(config.getJdUnionId(), goodId);
        } else if (PlatformType.Pdd.anyMatch(type.getCode())) {
            return getPrivilegeLinkByPdd(goodId);
        }
        return null;
    }

    private PrivilegeLinkVo getPrivilegeLinkByPdd(String goodId) {
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("appKey", config.getAppKey());
        paraMap.put("version", "v1.0.0");
        paraMap.put("goodsSign", goodId);
        String data = ApiClient.sendReq("https://openapi.dataoke.com/api/dels/pdd/kit/goods-prom-generate", config.getSecret(), paraMap);
        PrivilegeLinkVo result = new PrivilegeLinkVo();
        result.setPrivilegeUrl(ObjectUtil.defaultIfBlank(StrUtil.toString(JSONUtil.getByPath(JSONUtil.parse(data), "data.shortUrl")), null));
        return result;
    }

    private PrivilegeLinkVo getPrivilegeLinkByJd(String unionId, String materialId) {
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("appKey", config.getAppKey());
        paraMap.put("version", "v1.0.0");
        paraMap.put("unionId", unionId);
        paraMap.put("materialId", materialId);
        String data = ApiClient.sendReq("https://openapi.dataoke.com/api/dels/jd/kit/promotion-union-convert", config.getSecret(), paraMap);
        PrivilegeLinkVo result = new PrivilegeLinkVo();
        String privilegeUrl = ObjectUtil.defaultIfBlank(StrUtil.toString(JSONUtil.getByPath(JSONUtil.parse(data), "data.shortUrl")), null);
        String shortCode = LangUtils.lastElement(StrUtil.split(privilegeUrl, "/"));
        String content = HttpUtil.get(privilegeUrl);
        String jbaUrl = LangUtils.extract(StrUtil.format("https\\://u\\.jd\\.com/jda.*?{}", shortCode), content);
        String location = HttpUtil.createGet(jbaUrl).setFollowRedirects(false).execute().header("location");
        // location = https://item.m.jd.com/product/10035254492755.html?cu=true&utm_source=kong&utm_medium=jingfen&utm_campaign=t_1003815318_0a0a0a0a0&utm_term=72f4444ec290409eba12e651f7c2a4b4
        location = StrUtil.replace(location, "/product/", "/")
            .replace(".m.", ".");
        result.setPrivilegeUrl(location);
        return result;
    }


    private PrivilegeLinkVo getPrivilegeLinkByTaoBao(String goodId) {
        TreeMap<String, String> paraMap = new TreeMap<>();
        paraMap.put("appKey", config.getAppKey());
        paraMap.put("version", "v1.3.1");
        paraMap.put("goodsId", goodId);
        String data = ApiClient.sendReq("https://openapi.dataoke.com/api/tb-service/get-privilege-link", config.getSecret(), paraMap);
        PrivilegeLinkVo result = new PrivilegeLinkVo();
        String privilegeUrl = ObjectUtil.defaultIfBlank(StrUtil.toString(JSONUtil.getByPath(JSONUtil.parse(data), "data.itemUrl")), null);
        result.setPrivilegeUrl(privilegeUrl);
        return result;
    }


}
