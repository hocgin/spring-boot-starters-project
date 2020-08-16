package in.hocg.boot.message.core.transactional;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TransactionalMessageContext {
    private final static ThreadLocal<List<TransactionalMessage>> MESSAGE_POOL = ThreadLocal.withInitial(ArrayList::new);

    public static void add(TransactionalMessage message) {
        MESSAGE_POOL.get().add(message);
    }

    public static List<TransactionalMessage> get() {
        return Lists.newArrayList(MESSAGE_POOL.get());
    }

    public static void clear() {
        MESSAGE_POOL.get().clear();
    }

    public static List<TransactionalMessage> getAndClear() {
        final List<TransactionalMessage> messages = get();
        clear();
        return messages;
    }
}
