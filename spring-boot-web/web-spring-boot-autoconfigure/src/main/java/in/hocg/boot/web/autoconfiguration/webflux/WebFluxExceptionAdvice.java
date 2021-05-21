package in.hocg.boot.web.autoconfiguration.webflux;

import in.hocg.boot.web.autoconfiguration.advice.DefaultExceptionAdvice;
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
public class WebFluxExceptionAdvice extends DefaultExceptionAdvice {

}
