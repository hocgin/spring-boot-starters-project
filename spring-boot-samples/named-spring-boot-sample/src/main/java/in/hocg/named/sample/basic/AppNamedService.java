package in.hocg.named.sample.basic;

import com.google.common.collect.Maps;
import in.hocg.boot.named.ifc.NamedArgs;
import in.hocg.boot.named.ifc.NamedHandler;
import in.hocg.boot.named.annotation.NamedService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class AppNamedService implements NamedService {

    @NamedHandler(NamedConstants.Test)
    public Map<String, Object> loadTestData(NamedArgs args) throws InterruptedException {
        Map<String, Object> result = Maps.newHashMap();
        List<String> values = args.getValues();
        for (String value : values) {
            // Thread.sleep(100L);
            result.put(value, "xx-1#" + value);
        }
        return result;
    }

    @NamedHandler(NamedConstants.Test2)
    public Map<String, Object> loadTest2Data(NamedArgs args) throws InterruptedException {
        Map<String, Object> result = Maps.newHashMap();
        List<String> values = args.getValues();
        for (String value : values) {
            // Thread.sleep(100L);
            result.put(value, "xx-2#" + value);
        }
        return result;
    }

    @NamedHandler(NamedConstants.Test3)
    public Map<String, Object> loadTest3Data(NamedArgs args) throws InterruptedException {
        Map<String, Object> result = Maps.newHashMap();
        List<String> values = args.getValues();
        for (String value : values) {
            // Thread.sleep(100L);
            result.put(value, "xx-3#" + value);
        }
        throw new RuntimeException(" -> 异常");
//        return result;
    }
}
