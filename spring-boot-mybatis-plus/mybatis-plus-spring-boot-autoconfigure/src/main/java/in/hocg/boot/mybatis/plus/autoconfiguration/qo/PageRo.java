package in.hocg.boot.mybatis.plus.autoconfiguration.qo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by hocgin on 2020/5/28.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRo extends BasicRo {
    private Integer size = 20;
    private Integer page = 1;

    @JsonIgnore
    public <T> Page<T> ofPage() {
        return new Page<>(page, size);
    }
}
