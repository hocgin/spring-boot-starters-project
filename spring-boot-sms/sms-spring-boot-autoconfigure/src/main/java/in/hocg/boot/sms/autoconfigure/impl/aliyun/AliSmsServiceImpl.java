package in.hocg.boot.sms.autoconfigure.impl.aliyun;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import in.hocg.boot.sms.autoconfigure.core.SmsService;
import in.hocg.boot.sms.autoconfigure.exception.SmsException;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.request.SmsRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AliSmsServiceImpl implements SmsService, InitializingBean {
    private final String accessKey;
    private final String secretKey;
    private final String regionId;
    private IAcsClient client;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, secretKey);
        client = new DefaultAcsClient(profile);
    }

    @Override
    public void sendBatchSms(String text) {

    }

    @Override
    public String sendSms(@NonNull String phone, @NonNull SmsTemplate template, Map<String, String> vars) {
        String signName = template.getSignName();
        String templateCode = template.getTemplateCode();
        CommonRequest request = new SmsRequest(phone, signName, templateCode)
            .setVars(vars).build();
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            log.debug("响应内容: {}", data);
            return data;
        } catch (ClientException e) {
            throw new SmsException(e);
        }
    }
}
