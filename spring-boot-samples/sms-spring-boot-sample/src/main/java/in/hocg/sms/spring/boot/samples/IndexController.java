package in.hocg.sms.spring.boot.samples;

import in.hocg.boot.sms.autoconfigure.core.SmsBervice;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.request.BatchSmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2019/6/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class IndexController {
    private final SmsBervice service;

    @GetMapping("/send-sms")
    public String sendSms(@RequestParam("phone") String phone) {
        SmsTpl test = SmsTpl.Test;
        Map<String, String> vars = new HashMap<>();
        vars.put("code", "1111");
        return service.sendSms(phone, test.getSignName(), test.getTemplateCode(), vars);
    }

    @GetMapping("/send-batch-sms")
    public String sendBatchSms(@RequestParam("phone") String phone) {
        SmsTpl test = SmsTpl.Test;
        Map<String, String> vars = new HashMap<>();
        vars.put("code", "1111");

        ArrayList<BatchSmsRequest.Item> items = new ArrayList<>();
        items.add(new BatchSmsRequest.Item(phone, test.getSignName(), vars));
        return service.sendBatchSms(items, test.getTemplateCode());
    }
}
