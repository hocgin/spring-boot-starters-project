package in.hocg.boot.named.autoconfiguration.core;

/**
 * Created by hocgin on 2021/2/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface NamedCacheService {

    Object get(String key);

    void put(String key, Object value);
}
