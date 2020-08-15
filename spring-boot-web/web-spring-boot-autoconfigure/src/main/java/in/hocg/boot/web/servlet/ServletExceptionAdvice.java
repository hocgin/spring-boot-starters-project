package in.hocg.boot.web.servlet;

import in.hocg.boot.web.advice.DefaultExceptionAdvice;
import in.hocg.boot.web.result.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = "请求方法不支持";
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = e.getParameterName() + " 不能为空";
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult handleMissingServletRequestParameterException(HttpMediaTypeNotSupportedException e) {
        String message = "请求体内容不支持";
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ClientAbortException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleClientAbortException(ClientAbortException ex) {
        String message = "客户端连接中止异常";
        log.warn(message, ex);
    }
}
