package in.hocg.boot.named.autoconfiguration.core.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Created by hocgin on 2021/6/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class IPageNamedRowsConvert implements NamedRowsConvert{

    @Override
    public boolean isMatch(Object source) {
        return source instanceof IPage;
    }

    @Override
    public Object convert(Object source) {
        return ((IPage) source).getRecords();
    }
}
