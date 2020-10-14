package in.hocg.boot.sms.autoconfigure.impl.aliyun;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import in.hocg.boot.sms.autoconfigure.core.SmsService;
import in.hocg.boot.sms.autoconfigure.exception.SmsException;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.request.BatchSmsRequest;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.request.SmsRequest;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.response.SmsResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;

import javax.validation.constraints.NotNull;
import java.util.List;
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
    public String sendBatchSms(@NotNull List<BatchSmsRequest.Item> items, @NonNull String templateCode) {
        CommonRequest request = new BatchSmsRequest(templateCode)
            .setItems(items).build();
        try {
            CommonResponse response = client.getCommonResponse(request);
            SmsResponse responseData = JSONUtil.toBean(response.getData(), SmsResponse.class);
            if (!responseData.isSuccess()) {
                throw new SmsException(StrUtil.format("发送失败: 失败原因=[{}], Code=[{}]", responseData.getMessage(), responseData.getCode()));
            }
            return responseData.getBizId();
        } catch (ClientException e) {
            throw new SmsException(e);
        }
    }

    @Override
    public String sendSms(@NonNull String phone, @NonNull String signName, @NonNull String templateCode, Map<String, String> vars) {
        CommonRequest request = new SmsRequest(phone, signName, templateCode)
            .setTemplateParam(vars).build();
        try {
            CommonResponse response = client.getCommonResponse(request);
            SmsResponse responseData = JSONUtil.toBean(response.getData(), SmsResponse.class);
            if (!responseData.isSuccess()) {
                throw new SmsException(StrUtil.format("发送失败: 失败原因=[{}], Code=[{}]", responseData.getMessage(), responseData.getCode()));
            }
            return responseData.getBizId();
        } catch (ClientException e) {
            throw new SmsException(e);
        }
    }
}
