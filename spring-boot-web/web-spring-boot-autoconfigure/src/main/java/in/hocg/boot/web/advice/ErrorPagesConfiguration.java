package in.hocg.boot.web.advice;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.web.result.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@RestController
public class ErrorPagesConfiguration implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
        registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"));
        registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400"));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/error/404")
    public ExceptionResult error404() {
        return ExceptionResult.create(HttpStatus.NOT_FOUND.value(), "请求的资源不存在");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = "/error/500")
    public ExceptionResult error500() {
        log.error("服务异常ID: [{}]", IdUtil.randomUUID());
        return ExceptionResult.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统繁忙, 请稍后");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/error/400")
    public ExceptionResult error400() {
        return ExceptionResult.create(HttpStatus.BAD_REQUEST.value(), "请求有误");
    }
}
