package in.hocg.boot.named.autoconfiguration.aspect;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.vm.VM;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hocgin on 2021/6/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class NamedContext {
    private final ThreadLocal<ConcurrentHashSet<Long>> hashPool = ThreadLocal.withInitial(ConcurrentHashSet::new);
    private final ThreadLocal<AtomicInteger> count = ThreadLocal.withInitial(AtomicInteger::new);

    public boolean add(long id) {
        return hashPool.get().add(id);
    }

    public void push() {
        count.get().incrementAndGet();
    }

    public void pop() {
        if (count.get().decrementAndGet() == 0) {
            hashPool.get().clear();
        }
    }

    public boolean contains(long id) {
        return hashPool.get().contains(id);
    }

    public void clear() {
        hashPool.set(new ConcurrentHashSet<>());
        count.set(new AtomicInteger());
    }

    public long id(Object object) {
        return Objects.isNull(object) ? 0 : VM.current().addressOf(object);
    }

}
