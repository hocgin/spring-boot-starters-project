package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.function.Function;

@Data
@Accessors(chain = true)
public class IScrollExtra<H, T> implements Serializable {
    private H extra;
    private IScroll<T> data = PageUtils.emptyScroll();

    public static <H, T> IScrollExtra<H, T> of(H extra, IScroll<T> data) {
        return new IScrollExtra<H, T>().setExtra(extra).setData(data);
    }

    @SuppressWarnings("unchecked")
    public <R> IScrollExtra<H, R> convert(Function<? super T, ? extends R> mapper) {
        IScroll<R> newData = data.convert(mapper);
        return ((IScrollExtra<H, R>) this).setData(newData);
    }

    @SuppressWarnings("unchecked")
    public <R> IScrollExtra<R, T> fillExtra(Function<? super H, ? extends R> mapper) {
        R newExtra = mapper.apply(extra);
        return ((IScrollExtra<R, T>) this).setExtra(newExtra);
    }
}
