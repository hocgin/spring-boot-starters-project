package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/11/29
 * email: hocgin@gmail.com
 * 滚动翻页
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ScrollRo extends BasicRo {
    private Serializable nextId;
    private Integer size = 20;

    @JsonIgnore
    public <T> Page<T> ofPage() {
        return new Page<>(1, size, false);
    }

}
