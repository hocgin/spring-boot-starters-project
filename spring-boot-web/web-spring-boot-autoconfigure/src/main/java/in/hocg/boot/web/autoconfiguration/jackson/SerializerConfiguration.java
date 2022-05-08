package in.hocg.boot.web.autoconfiguration.jackson;

import in.hocg.boot.web.autoconfiguration.jackson.xlong.LongDeserializer;
import in.hocg.boot.web.autoconfiguration.jackson.xlong.LongSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hocgin on 2020/9/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Configuration
public class SerializerConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
//            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
//            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
            builder.serializerByType(Long.class, new LongSerializer());
            builder.deserializerByType(Long.class, new LongDeserializer());
        };
    }

}
