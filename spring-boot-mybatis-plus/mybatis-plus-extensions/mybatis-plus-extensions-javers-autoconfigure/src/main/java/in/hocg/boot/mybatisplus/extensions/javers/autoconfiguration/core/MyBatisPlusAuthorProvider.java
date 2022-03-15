package in.hocg.boot.mybatisplus.extensions.javers.autoconfiguration.core;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.context.UserContextHolder;
import org.javers.spring.auditable.AuthorProvider;

/**
 * Created by hocgin on 2022/3/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MyBatisPlusAuthorProvider implements AuthorProvider {
    @Override
    public String provide() {
        return StrUtil.toString(UserContextHolder.getUserId());
    }
}
