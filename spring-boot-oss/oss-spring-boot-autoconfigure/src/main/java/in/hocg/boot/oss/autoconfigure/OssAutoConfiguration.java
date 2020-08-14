package in.hocg.boot.oss.autoconfigure;

import in.hocg.boot.oss.autoconfigure.properties.OssProperties;
import in.hocg.boot.oss.autoconfigure.properties.OssType;
import in.hocg.boot.oss.autoconfigure.core.OssFileService;
import in.hocg.boot.oss.autoconfigure.impl.AliOssFileServiceImpl;
import in.hocg.boot.oss.autoconfigure.impl.QiNiuOssFileServiceImpl;
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
        OssType ossType = properties.getType();
        switch (ossType) {
            case QiNiu:
                return new QiNiuOssFileServiceImpl(accessKey, secretKey, space, domain);
            case AliYun:
                return new AliOssFileServiceImpl(accessKey, secretKey, space, domain);
            default:
                throw new IllegalArgumentException("OSS类型[" + ossType + "]不支持");
        }
    }

}
