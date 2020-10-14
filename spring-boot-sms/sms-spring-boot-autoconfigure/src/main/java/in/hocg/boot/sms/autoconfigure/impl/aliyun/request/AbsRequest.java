package in.hocg.boot.sms.autoconfigure.impl.aliyun.request;

import com.aliyuncs.CommonRequest;

/**
 * Created by hocgin on 2020/10/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface AbsRequest {
    CommonRequest build();
}
