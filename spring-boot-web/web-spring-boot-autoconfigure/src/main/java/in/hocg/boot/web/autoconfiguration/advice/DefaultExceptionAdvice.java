package in.hocg.boot.web.autoconfiguration.advice;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.web.exception.ServiceException;
import in.hocg.boot.web.exception.UnAuthenticationException;
import in.hocg.boot.web.result.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class DefaultExceptionAdvice {

    @ExceptionHandler(UnAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResult<Void> handleUnAuthenticationException(UnAuthenticationException ex) {
        String message = "请先进行登陆";
        log.warn(message, ex);
        return create(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleServiceException(ServiceException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<Void> handleException(Exception e) {
        String message = StrUtil.emptyToDefault(e.getMessage(), "系统繁忙, 请稍后");
        log.error("服务异常ID: [{}]", IdUtil.randomUUID(), e);
        return create(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "入参数据格式错误";
        log.warn(message, ex);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = handleValidException(e.getBindingResult());
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleBindException(BindException e) {
        String message = handleValidException(e.getBindingResult());
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    protected ExceptionResult<Void> create(HttpStatus httpStatus, String message) {
        return ExceptionResult.fail(httpStatus.value(), message);
    }

    private String handleValidException(BindingResult result) {
        FieldError fieldError = result.getFieldError();
        String message = null;
        if (Objects.nonNull(fieldError)) {
            message = fieldError.getDefaultMessage();
        }
        return message;
    }
}
