package in.hocg.boot.named.spring.boot.autoconfiguration.core;

import com.baomidou.mybatisplus.core.metadata.IPage;

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
