package in.hocg.boot.message.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.MessageHeaders;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class DefaultMessage implements org.springframework.messaging.Message<Object> {
    @Setter
    @Getter
    private String destination;
    @Setter
    private Object payload;
    @Setter
    private Map<String, Object> headers = Collections.emptyMap();

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return new MessageHeaders(headers);
    }
}
