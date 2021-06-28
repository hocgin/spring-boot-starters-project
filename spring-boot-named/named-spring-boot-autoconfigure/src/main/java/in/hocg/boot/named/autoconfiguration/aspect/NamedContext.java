package in.hocg.boot.named.autoconfiguration.aspect;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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
    private final ThreadLocal<ConcurrentHashSet<Integer>> hashPool = ThreadLocal.withInitial(ConcurrentHashSet::new);
    private final ThreadLocal<AtomicInteger> count = ThreadLocal.withInitial(AtomicInteger::new);

    public boolean add(int hash) {
        return hashPool.get().add(hash);
    }

    public void push() {
        count.get().incrementAndGet();
    }

    public void pop() {
        if (count.get().decrementAndGet() == 0) {
            hashPool.get().clear();
        }
    }

    public boolean contains(int hash) {
        return hashPool.get().contains(hash);
    }

    public void clear() {
        hashPool.set(new ConcurrentHashSet<>());
        count.set(new AtomicInteger());
    }

    public int hash(Object object) {
        return Objects.isNull(object) ? 0 : object.hashCode();
    }

}
