package in.hocg.boot.web.jackson;

import in.hocg.boot.web.jackson.localdatetime.LocalDateTimeDeserializer;
import in.hocg.boot.web.jackson.localdatetime.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2020/9/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
public class SerializerConfiguration {

//    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        };
    }

}
