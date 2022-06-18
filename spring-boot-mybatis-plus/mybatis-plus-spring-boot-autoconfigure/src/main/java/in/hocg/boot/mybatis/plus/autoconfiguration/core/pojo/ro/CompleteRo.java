package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2020/3/31.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CompleteRo extends BasicRo {
    private int size = 30;

    @JsonIgnore
    public <T> Page<T> ofPage() {
        return new Page<>(1, this.size, false);
    }
}
