package in.hocg.boot.named.autoconfiguration.core.convert;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.vo.IScroll;

/**
 * Created by hocgin on 2022/4/17
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class IScrollNamedRowsConvert implements NamedRowsConvert {
    @Override
    public boolean isMatch(Class<?> aClass) {
        return IScroll.class.isAssignableFrom(aClass);
    }

    @Override
    public Object convert(Object o) {
        return ((IScroll) o).getRecords();
    }
}
