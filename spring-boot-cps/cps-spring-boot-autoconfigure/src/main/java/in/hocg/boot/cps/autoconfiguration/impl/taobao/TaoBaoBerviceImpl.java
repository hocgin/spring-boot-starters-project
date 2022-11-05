package in.hocg.boot.cps.autoconfiguration.impl.taobao;

import cn.hutool.core.util.StrUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;
import in.hocg.boot.cps.autoconfiguration.impl.CpsBervice;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class TaoBaoBerviceImpl implements CpsBervice {
    private DefaultTaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", "32115974", "99e042009bed640aabbe762b06b186a2");

    public static void main(String[] args) {
        new TaoBaoBerviceImpl().getGoodInfo(List.of("563867605080"));
    }


    @SneakyThrows
    public List<TbkItemInfoGetResponse.NTbkItem> getGoodInfo(List<String> ids) {
        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
        request.setPlatform(2L);
        request.setNumIids(StrUtil.join(",", ids));
        TbkItemInfoGetResponse response = client.execute(request);
        log.debug("{}", response);
        return response.getResults();
    }

//    public void convertUrl() {
//        TaobaoTbkItemConvertRequest req = new TaobaoTbkItemConvertRequest();
//        req.setFields("user_id,click_url");
//        req.setUserIds("123,456");
//        req.setPlatform(1L);
//        req.setAdzoneId(123L);
//        req.setSubPid("demo");
//        req.setUnid("demo");
//        TbkShopShareConvertResponse rsp = client.execute(req);
//    }
}
