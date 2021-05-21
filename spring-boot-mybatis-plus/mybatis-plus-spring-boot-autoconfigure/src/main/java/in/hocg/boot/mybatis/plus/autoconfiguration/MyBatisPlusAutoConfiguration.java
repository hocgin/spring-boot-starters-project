package in.hocg.boot.mybatis.plus.autoconfiguration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.google.common.collect.Sets;
import in.hocg.boot.mybatis.plus.autoconfiguration.properties.MyBatisPlusProperties;
import in.hocg.boot.utils.LangUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.util.Set;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(MyBatisPlusProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MyBatisPlusAutoConfiguration {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    @Primary
    public MybatisPlusProperties myBatisPlusProperties(MybatisPlusProperties properties) {
        GlobalConfig globalConfig = LangUtils.getOrDefault(properties.getGlobalConfig(), new GlobalConfig());
        globalConfig.setBanner(false);
        properties.setGlobalConfig(globalConfig);

        Set<String> locations = Sets.newHashSet(properties.getMapperLocations());
        locations.add("classpath*:/**/xml/*.xml");
        properties.setMapperLocations(locations.toArray(new String[]{}));
        return properties;
    }
}
