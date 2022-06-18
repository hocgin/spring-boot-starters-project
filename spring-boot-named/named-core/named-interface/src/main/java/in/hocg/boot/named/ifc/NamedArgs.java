package in.hocg.boot.named.ifc;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/8/13
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class NamedArgs {
    private List<Object> values = Collections.emptyList();
    private String[] args = new String[]{};

    public <T> List<T> getValues() {
        return (List<T>) values;
    }
}
