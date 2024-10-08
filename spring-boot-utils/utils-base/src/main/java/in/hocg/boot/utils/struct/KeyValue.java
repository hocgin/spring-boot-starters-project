package in.hocg.boot.utils.struct;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/2/25.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class KeyValue implements Serializable {
    private Serializable key;
    private Object value;
}
