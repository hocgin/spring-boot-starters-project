package in.hocg.boot.named.spring.boot.autoconfiguration.ifc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class NamedArgs {
    private List<Object> values = Collections.emptyList();
    private String[] args = new String[]{};

    public <T> List<T> getValues() {
        return (List<T>) values;
    }
}
