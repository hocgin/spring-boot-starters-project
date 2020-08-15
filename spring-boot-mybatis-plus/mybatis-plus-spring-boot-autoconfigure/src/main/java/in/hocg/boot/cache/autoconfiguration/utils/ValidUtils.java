package in.hocg.boot.cache.autoconfiguration.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

/**
 * Created by hocgin on 2020/2/15.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ValidUtils {

    public void notNull(Object object, String message) {
        try {
            Assert.notNull(object, message);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    public void isNull(Object object, String message) {
        try {
            Assert.isNull(object, message);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    public void isTrue(boolean expression, String message) {
        try {
            Assert.isTrue(expression, message);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void isFalse(boolean expression, String message) {
        try {
            Assert.isTrue(!expression, message);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void fail(String message) {
        throw new IllegalArgumentException(message);
    }

    public void fail(Exception e) {
        fail(e.getMessage());
    }

}
