package in.hocg.boot.mybatis.plus.autoconfiguration.qo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by hocgin on 2020/3/31.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompleteRo extends BasicRo {
    private int size = 30;

    @JsonIgnore
    public Page ofPage() {
        return new Page<>(1, this.size, false);
    }
}
