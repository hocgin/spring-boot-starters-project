package in.hocg.boot.mybatis.plus.extensions.httplog.autoconfiguration;

import in.hocg.boot.logging.autoconfiguration.core.LoggerListener;
import in.hocg.boot.mybatis.plus.extensions.httplog.support.LoggerListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2022/3/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
public class HttpLogMybatisPlusExtAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "in.hocg.boot.logging.autoconfiguration.core.LoggerListener")
    public LoggerListener loggerListener() {
        return new LoggerListenerImpl();
    }
}
