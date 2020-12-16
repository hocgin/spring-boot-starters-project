package in.hocg.boot.validation.autoconfigure.core;

import in.hocg.boot.utils.LangUtils;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/10/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ICode {

    Serializable getCode();

    default boolean eq(Serializable val) {
        final Serializable code = this.getCode();
        if (code instanceof String) {
            return LangUtils.equals((String) code, (String) val);
        } else if (code instanceof Integer) {
            return LangUtils.equals((Integer) code, (Integer) val);
        }
        return false;
    }
}
