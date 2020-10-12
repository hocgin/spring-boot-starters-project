package in.hocg.boot.sms.autoconfigure;

import in.hocg.boot.sms.autoconfigure.core.SmsService;
import in.hocg.boot.sms.autoconfigure.impl.aliyun.AliSmsServiceImpl;
import in.hocg.boot.sms.autoconfigure.properties.SmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2019/6/12.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = SmsProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(SmsProperties.class)
@RequiredArgsConstructor
public class SmsAutoConfiguration {
    private final SmsProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public SmsService ossFileService() {
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String regionId = properties.getRegionId();
        SmsProperties.SmsType type = properties.getType();
        switch (type) {
            case AliYun:
                return new AliSmsServiceImpl(accessKey, secretKey, regionId);
            default:
                throw new IllegalArgumentException("OSS 类型[" + type + "]不支持");
        }
    }

}
