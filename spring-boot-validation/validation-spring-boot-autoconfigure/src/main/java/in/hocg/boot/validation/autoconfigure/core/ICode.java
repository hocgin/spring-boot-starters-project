package in.hocg.boot.validation.autoconfigure.core;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/10/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface ICode {

    boolean eq(Serializable code);
}
