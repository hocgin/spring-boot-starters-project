package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.utils.PageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.function.Function;

@Data
@Accessors(chain = true)
public class IPageExtra<H, T> implements Serializable {
    private H extra;
    private IPage<T> data = PageUtils.emptyPage(1, 10L);

    public static <H, T> IPageExtra<H, T> of(H extra, IPage<T> data) {
        return new IPageExtra<H, T>().setExtra(extra).setData(data);
    }

    @SuppressWarnings("unchecked")
    public <R> IPageExtra<H, R> convert(Function<? super T, ? extends R> mapper) {
        IPage<R> newData = data.convert(mapper);
        return ((IPageExtra<H, R>) this).setData(newData);
    }

    @SuppressWarnings("unchecked")
    public <R> IPageExtra<R, T> fillExtra(Function<? super H, ? extends R> mapper) {
        R newExtra = mapper.apply(extra);
        return ((IPageExtra<R, T>) this).setExtra(newExtra);
    }
}
