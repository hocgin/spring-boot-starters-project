package in.hocg.boot.cache.autoconfiguration;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * Created by hocgin on 2020/1/5.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public abstract class AbstractEntity<T extends AbstractEntity<?>> extends Model<T> {

    @Override
    public java.io.Serializable pkVal() {
        return super.pkVal();
    }
}
