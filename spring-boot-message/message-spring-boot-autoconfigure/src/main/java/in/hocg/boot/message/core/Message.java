package in.hocg.boot.message.core;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface Message {

    default String getDestination() {
        return Message.class.getSimpleName();
    }

    default Map<String, Object> getHeaders() {
        return Collections.emptyMap();
    }
}
