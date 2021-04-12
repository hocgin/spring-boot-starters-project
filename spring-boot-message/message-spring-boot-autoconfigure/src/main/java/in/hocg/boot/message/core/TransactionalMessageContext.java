package in.hocg.boot.message.core;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TransactionalMessageContext<T> {
    private final ThreadLocal<List<T>> messagePool = ThreadLocal.withInitial(ArrayList::new);

    public void add(T message) {
        messagePool.get().add(message);
    }

    public List<T> get() {
        return Lists.newArrayList(messagePool.get());
    }

    public void clear() {
        messagePool.get().clear();
    }

    public List<T> getAndClear() {
        final List<T> messages = get();
        clear();
        return messages;
    }
}
