package in.hocg.boot.cps.autoconfiguration;

import in.hocg.boot.cps.autoconfiguration.impl.CpsBervice;
import in.hocg.boot.cps.autoconfiguration.impl.dataoke.DaTaoKeBerviceImpl;
import in.hocg.boot.cps.autoconfiguration.properties.CpsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@ConditionalOnProperty(prefix = CpsProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(CpsProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class CpsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CpsBervice cpsBervice(CpsProperties properties) {
        if (CpsProperties.Type.DaTaoKe.equals(properties.getType())) {
            return new DaTaoKeBerviceImpl(properties.getDaTaoKe());
        }
        throw new UnsupportedOperationException();
    }


}
