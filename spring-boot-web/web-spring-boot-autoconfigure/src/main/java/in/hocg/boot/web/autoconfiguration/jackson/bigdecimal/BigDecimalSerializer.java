package in.hocg.boot.web.autoconfiguration.jackson.bigdecimal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@JsonComponent
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {
    public static final String DEFAULT_FORMAT = "###,##0.00";
    private String useFormat = BigDecimalSerializer.DEFAULT_FORMAT;

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new DecimalFormat(useFormat).format(bigDecimal));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (Objects.isNull(beanProperty)) {
            return serializerProvider.findNullValueSerializer(null);
        }

        if (Objects.equals(beanProperty.getType().getRawClass(), BigDecimal.class)) {
            BigDecimalFormat annotation = beanProperty.getAnnotation((BigDecimalFormat.class));
            if (Objects.isNull(annotation)) {
                annotation = beanProperty.getContextAnnotation(BigDecimalFormat.class);
            }
            BigDecimalSerializer serializer = new BigDecimalSerializer();
            if (Objects.nonNull(annotation)) {
                serializer.useFormat = annotation.value();
            }
            return serializer;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
