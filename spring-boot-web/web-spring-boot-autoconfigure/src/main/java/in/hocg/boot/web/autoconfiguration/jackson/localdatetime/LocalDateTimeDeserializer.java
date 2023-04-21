package in.hocg.boot.web.autoconfiguration.jackson.localdatetime;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Created by hocgin on 2019/5/22.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (Objects.nonNull(p) && StrUtil.isNotBlank(p.getText())) {
            String text = p.getText();
            if (NumberUtil.isLong(text)) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(p.getLongValue()), ZoneOffset.of("+8"));
            }
            return Convert.toLocalDateTime(text);
        }
        return null;
    }
}
