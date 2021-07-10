package in.hocg.boot.named.autoconfiguration.core.convert;

import java.util.Optional;

/**
 * Created by hocgin on 2021/6/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class OptionalNamedRowsConvert implements NamedRowsConvert{

    @Override
    public boolean isMatch(Class<?> aClazz) {
        return Optional.class.isAssignableFrom(aClazz);
    }

    @Override
    public Object convert(Object source) {
        return ((Optional) source).orElse(null);
    }
}
