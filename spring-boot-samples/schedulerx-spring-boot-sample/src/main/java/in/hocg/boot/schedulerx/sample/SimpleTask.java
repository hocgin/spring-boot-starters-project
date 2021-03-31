package in.hocg.boot.schedulerx.sample;

import com.alibaba.edas.schedulerx.ProcessResult;
import com.alibaba.edas.schedulerx.ScxSimpleJobContext;
import com.alibaba.edas.schedulerx.ScxSimpleJobProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2021/3/31
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Component
public class SimpleTask implements ScxSimpleJobProcessor {

    @Override
    public ProcessResult process(ScxSimpleJobContext scxSimpleJobContext) {
        System.out.println("OK");
        return new ProcessResult(true);
    }
}
