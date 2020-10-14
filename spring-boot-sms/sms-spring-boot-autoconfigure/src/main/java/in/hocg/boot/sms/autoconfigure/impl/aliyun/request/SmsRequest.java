package in.hocg.boot.sms.autoconfigure.impl.aliyun.request;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2020/10/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class SmsRequest implements AbsRequest {
    private final String phoneNumbers;
    private final String signName;
    private final String templateCode;
    private Map<String, String> templateParam = Collections.emptyMap();

    @Override
    public CommonRequest build() {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        if (CollectionUtil.isNotEmpty(templateParam)) {
            request.putQueryParameter("TemplateParam", JSONUtil.toJsonStr(templateParam));
        }
        return request;
    }
}
