package in.hocg.boot.sms.autoconfigure.core;

import in.hocg.boot.sms.autoconfigure.impl.aliyun.request.BatchSmsRequest;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface SmsBervice {

    String sendBatchSms(@NotNull List<BatchSmsRequest.Item> items, @NonNull String templateCode);

    String sendSms(@NonNull String phone, @NonNull String signName, @NonNull String templateCode, Map<String, String> vars);
}
