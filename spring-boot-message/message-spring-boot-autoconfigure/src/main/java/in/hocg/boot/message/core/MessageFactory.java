package in.hocg.boot.message.core;

import cn.hutool.core.lang.Validator;
import in.hocg.boot.message.annotation.MessageDestination;
import org.apache.logging.log4j.util.Strings;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface MessageFactory {

    default org.springframework.messaging.Message convert(Message payload) {
        final Class<?> aClass = payload.getClass();
        String destination = aClass.getSimpleName();
        MessageDestination destinationAnnotation = aClass.getAnnotation(MessageDestination.class);
        if (Objects.nonNull(destinationAnnotation)) {
            String destinationVal = destinationAnnotation.value();
            if (Strings.isNotBlank(destinationVal)) {
                destination = destinationVal;
            }
        }

        Map<String, Object> headers = Collections.emptyMap();
        if (Message.class.isAssignableFrom(aClass)) {
            destination = payload.getDestination();
            headers = payload.getHeaders();
        }

        Validator.isNotEmpty(destination);

        final DefaultMessage message = new DefaultMessage();
        message.setDestination(destination);
        message.setPayload(payload);
        message.setHeaders(headers);
        return message;
    }
}
