package in.hocg.boot.sentinel.sample;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by hocgin on 2021/7/24
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@ResponseBody
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BlockException.class)
    public String handleBlockException(BlockException e) {
        return "系统繁忙，降级处理";
    }

    @ExceptionHandler(FlowException.class)
    public String handleFlowException(FlowException e) {
        return "系统繁忙，降级处理";
    }
}
