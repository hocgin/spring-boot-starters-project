package in.hocg.boot.web.servlet;

import in.hocg.boot.web.advice.DefaultExceptionAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RestControllerAdvice
public class ServletExceptionAdvice extends DefaultExceptionAdvice {

}
