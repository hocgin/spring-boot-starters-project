package in.hocg.boot.named.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.utils.LangUtils;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ClassName {
    public static final String IPageClassName = "com.baomidou.mybatisplus.core.metadata.IPage";

    public static boolean isIPageClass(Object object) {
        return LangUtils.isPresent(IPageClassName) && object instanceof IPage;
    }
}
