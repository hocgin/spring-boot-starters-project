package in.hocg.boot.named.autoconfiguration.core.convert;

/**
 * Created by hocgin on 2021/6/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface NamedRowsConvert {
    boolean isMatch(Class<?> source);

    Object convert(Object source);
}
