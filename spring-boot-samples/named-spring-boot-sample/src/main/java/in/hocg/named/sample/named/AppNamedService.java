package in.hocg.named.sample.named;

import com.google.common.collect.Maps;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedArgs;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedHandler;
import in.hocg.boot.named.spring.boot.autoconfiguration.ifc.NamedService;
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
    public Map<String, Object> loadTestData(NamedArgs args) {
        Map<String, Object> result = Maps.newHashMap();
        List<String> values = args.getValues();
        for (String value : values) {
            result.put(value, "Value#" + value);
        }
        return result;
    }
}
