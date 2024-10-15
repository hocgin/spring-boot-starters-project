package in.hocg.boot.geoip.autoconfiguration;

import com.maxmind.geoip2.DatabaseReader;
import in.hocg.boot.geoip.autoconfiguration.core.GeoIpBervice;
import in.hocg.boot.geoip.autoconfiguration.core.GeoIpBerviceImpl;
import in.hocg.boot.geoip.autoconfiguration.properties.GeoIpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = GeoIpProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(GeoIpProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class GeoIpAutoConfiguration {
    private final GeoIpProperties properties;

    @Bean

    public GeoIpBervice geoIpBervice() throws IOException {
        try {
            String ipPath = properties.getCountryPath();
            String litePath = properties.getCityPath();
            File file = new File(ipPath);
            FileInputStream fin = new FileInputStream(file);
            DatabaseReader countryReader = new DatabaseReader.Builder(fin).build();

            File fileCity = new File(litePath);
            FileInputStream finCity = new FileInputStream(fileCity);
            DatabaseReader cityReader = new DatabaseReader.Builder(finCity).build();
            return new GeoIpBerviceImpl(countryReader, cityReader);
        } catch (Exception e) {
            log.error("GeoIpBerviceImpl 初始化失败: {}", e.getMessage());
            return new GeoIpBerviceImpl(null, null);
        }
    }
}
