package in.hocg.boot.mybatis.plus.extensions.httplog.convert;

import cn.hutool.core.bean.BeanUtil;
import in.hocg.boot.mybatis.plus.extensions.httplog.entity.HttpLog;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.CreateLogRo;
import in.hocg.boot.mybatis.plus.extensions.httplog.pojo.ro.DoneLogRo;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Component
public class HttpLogMpeConvert {
    public HttpLog asHttpLog(CreateLogRo ro) {
        return BeanUtil.copyProperties(ro, HttpLog.class);
    }

    public HttpLog asHttpLog(DoneLogRo ro) {
        return BeanUtil.copyProperties(ro, HttpLog.class);
    }
}
