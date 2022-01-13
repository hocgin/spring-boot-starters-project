package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created by hocgin on 2021/11/29
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class IScroll<T> implements Serializable {
    /**
     * 是否有下一页
     */
    private Boolean hasMore = false;
    /**
     * 上一页的最后ID(> ID)
     */
    private Serializable nextId;
    /**
     * 数据
     */
    private List<T> records = Collections.emptyList();

    @SuppressWarnings("unchecked")
    public <R> IScroll<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((IScroll<R>) this).setRecords(collect);
    }
}
