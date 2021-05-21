package in.hocg.boot.web.autoconfiguration.jackson.bigdecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@JsonComponent
public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if (Objects.isNull(text)) {
            return null;
        }
        return new BigDecimal(text.replaceAll(",",""));
    }
}
