package in.hocg.xxljob.spring.boot.samples.jobs;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2021/1/23
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DemoServiceJobs {

    @XxlJob("testJob")
    public ReturnT<String> testJob(String param) {
        XxlJobLogger.log("hello world" + param);
        return ReturnT.SUCCESS;
    }

}
