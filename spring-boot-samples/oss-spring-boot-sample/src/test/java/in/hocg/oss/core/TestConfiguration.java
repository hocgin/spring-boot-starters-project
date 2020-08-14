package in.hocg.oss.core;

import in.hocg.oss.spring.boot.samples.Application;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = Application.class)
@EnableAutoConfiguration
public class TestConfiguration {
}
