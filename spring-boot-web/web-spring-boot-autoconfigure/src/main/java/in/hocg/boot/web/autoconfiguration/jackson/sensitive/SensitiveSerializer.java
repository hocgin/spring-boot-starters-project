package in.hocg.boot.web.autoconfiguration.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import in.hocg.boot.utils.SensitiveUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@JsonComponent
@RequiredArgsConstructor
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private final SensitiveType type;
    private final String mask;
    private final Integer prefixNoMaskSize;
    private final Integer suffixNoMaskSize;


    @Override
    public void serialize(String text, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (type) {
            case CHINESE_NAME: {
                jsonGenerator.writeString(SensitiveUtils.chineseName(text));
                break;
            }
            case ID_CARD: {
                jsonGenerator.writeString(SensitiveUtils.idCardNum(text));
                break;
            }
            case TELEPHONE: {
                jsonGenerator.writeString(SensitiveUtils.telephone(text));
                break;
            }
            case MOBILE_PHONE: {
                jsonGenerator.writeString(SensitiveUtils.mobilePhone(text));
                break;
            }
            case ADDRESS: {
                jsonGenerator.writeString(SensitiveUtils.address(text));
                break;
            }
            case EMAIL: {
                jsonGenerator.writeString(SensitiveUtils.email(text));
                break;
            }
            case BANK_CARD: {
                jsonGenerator.writeString(SensitiveUtils.bankCard(text));
                break;
            }
            case PASSWORD: {
                jsonGenerator.writeString(SensitiveUtils.password(text));
                break;
            }
            case KEY: {
                jsonGenerator.writeString(SensitiveUtils.key(text));
                break;
            }
            case CUSTOMER: {
                jsonGenerator.writeString(SensitiveUtils.custom(text, prefixNoMaskSize, suffixNoMaskSize, mask));
                break;
            }
            default:
                throw new UnsupportedOperationException("sensitive type error: " + type);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (Objects.isNull(beanProperty)) {
            return serializerProvider.findNullValueSerializer(null);
        }

        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
            if (Objects.isNull(sensitive)) {
                sensitive = beanProperty.getContextAnnotation(Sensitive.class);
            }
            if (Objects.nonNull(sensitive)) {
                return new SensitiveSerializer(sensitive.type(), sensitive.mask(), sensitive.prefixNoMaskSize(), sensitive.suffixNoMaskSize());
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
