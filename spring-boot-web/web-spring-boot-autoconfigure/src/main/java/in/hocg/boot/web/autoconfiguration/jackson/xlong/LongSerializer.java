package in.hocg.boot.web.autoconfiguration.jackson.xlong;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created by hocgin on 2019/5/22.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class LongSerializer extends JsonSerializer<Long> {
    @Override
    public void serialize(Long val, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (ObjectUtil.isNull(val)) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(val.toString());
        }
    }
}
