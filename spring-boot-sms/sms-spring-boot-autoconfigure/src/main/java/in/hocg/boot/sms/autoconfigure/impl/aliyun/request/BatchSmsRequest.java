package in.hocg.boot.sms.autoconfigure.impl.aliyun.request;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import in.hocg.boot.sms.autoconfigure.exception.SmsException;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2020/10/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class BatchSmsRequest implements AbsRequest {
    private final String templateCode;
    private List<BatchSmsRequest.Item> items = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Item {
        private String phoneNumbers;
        private String signName;
        private Map<String, String> templateParam = Collections.emptyMap();

        public Item(String phoneNumbers, String signName) {
            this.phoneNumbers = phoneNumbers;
            this.signName = signName;
        }

        public Item(String phoneNumbers, String signName, Map<String, String> templateParam) {
            this.phoneNumbers = phoneNumbers;
            this.signName = signName;
            this.templateParam = templateParam;
        }
    }

    @Override
    public CommonRequest build() {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendBatchSms");
        List<Item> items = getItems();
        if (CollectionUtil.isEmpty(items)) {
            throw new SmsException("参数错误, 未指定接收目标");
        }
        List<String> phoneNumbers = new ArrayList<>();
        List<String> signNames = new ArrayList<>();
        List<Map<String, String>> templateParams = new ArrayList<>();

        for (Item item : items) {
            phoneNumbers.add(item.getPhoneNumbers());
            signNames.add(item.getSignName());
            templateParams.add(item.getTemplateParam());
        }

        request.putQueryParameter("PhoneNumberJson", JSONUtil.toJsonStr(phoneNumbers));
        request.putQueryParameter("SignNameJson", JSONUtil.toJsonStr(signNames));
        request.putQueryParameter("TemplateCode", templateCode);
        if (CollectionUtil.isNotEmpty(templateParams)) {
            request.putQueryParameter("TemplateParamJson", JSONUtil.toJsonStr(templateParams));
        }
        return request;
    }

}
