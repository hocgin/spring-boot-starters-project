package in.hocg.boot.oss.autoconfigure;

import in.hocg.boot.oss.autoconfigure.core.OssFileService;
import in.hocg.boot.oss.autoconfigure.impl.AliOssFileServiceImpl;
import in.hocg.boot.oss.autoconfigure.impl.QiNiuOssFileServiceImpl;
import in.hocg.boot.oss.autoconfigure.properties.OssProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
@Log
@Configuration
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(OssProperties.class)
@RequiredArgsConstructor
public class OssAutoConfiguration {
    private final OssProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public OssFileService ossFileService() {
        String accessKey = properties.getAccessKey();
        String secretKey = properties.getSecretKey();
        String space = properties.getSpace();
        String domain = properties.getDomain();
        OssProperties.OssType type = properties.getType();
        switch (type) {
            case QiNiu:
                return new QiNiuOssFileServiceImpl(accessKey, secretKey, space, domain);
            case AliYun:
                return new AliOssFileServiceImpl(accessKey, secretKey, space, domain);
            default:
                throw new IllegalArgumentException("OSS 类型[" + type + "]不支持");
        }
    }

}
