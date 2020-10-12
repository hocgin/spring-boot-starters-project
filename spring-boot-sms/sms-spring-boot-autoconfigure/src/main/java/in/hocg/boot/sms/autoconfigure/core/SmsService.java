package in.hocg.boot.sms.autoconfigure.core;

import in.hocg.boot.sms.autoconfigure.impl.aliyun.SmsTemplate;
import lombok.NonNull;

import java.util.Map;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface SmsService {

    void sendBatchSms(String text);

    String sendSms(@NonNull String phone, @NonNull SmsTemplate template, Map<String, String> vars);
}
