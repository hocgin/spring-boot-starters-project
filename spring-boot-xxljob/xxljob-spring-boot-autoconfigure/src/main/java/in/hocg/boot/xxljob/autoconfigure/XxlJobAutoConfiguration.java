package in.hocg.boot.xxljob.autoconfigure;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import in.hocg.boot.xxljob.autoconfigure.properties.XxlJobProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class XxlJobAutoConfiguration {
    private final XxlJobProperties properties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> [开始] xxl-job config init.");
        String adminAddresses = properties.getAdminAddresses();
        String address = properties.getAddress();
        String appname = properties.getAppname();
        String ip = properties.getIp();
        Integer port = properties.getPort();
        String accessToken = properties.getAccessToken();
        String logPath = properties.getLogPath();
        Integer logRetentionDays = properties.getLogRetentionDays();

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        log.info(">>>>>>>>>>> [完成] xxl-job config init. adminAddresses = {}, hostname = {}, address = {}", adminAddresses, ip + ":" + port, address);
        return xxlJobSpringExecutor;
    }
}
