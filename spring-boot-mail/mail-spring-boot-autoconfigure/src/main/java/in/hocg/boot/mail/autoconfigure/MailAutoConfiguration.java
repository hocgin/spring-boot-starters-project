package in.hocg.boot.mail.autoconfigure;

import in.hocg.boot.mail.autoconfigure.core.MailBervice;
import in.hocg.boot.mail.autoconfigure.impl.MailBerviceImpl;
import in.hocg.boot.mail.autoconfigure.properties.MailProperties;
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
@ConditionalOnProperty(prefix = MailProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor
public class MailAutoConfiguration {
    private final MailProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public MailBervice mailBervice() {
        return new MailBerviceImpl(properties);
    }
}
