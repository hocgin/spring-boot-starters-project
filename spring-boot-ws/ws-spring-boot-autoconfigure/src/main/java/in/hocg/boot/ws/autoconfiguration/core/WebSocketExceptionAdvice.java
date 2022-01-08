package in.hocg.boot.ws.autoconfiguration.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import in.hocg.boot.utils.exception.ServiceException;
import in.hocg.boot.utils.exception.UnAuthenticationException;
import in.hocg.boot.utils.struct.result.ExceptionResult;
import in.hocg.boot.ws.autoconfiguration.core.constant.StringConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * Created by hocgin on 2022/1/8
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RestControllerAdvice
public class WebSocketExceptionAdvice {

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(UnAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ExceptionResult<Void> handleUnAuthenticationException(UnAuthenticationException ex) {
        String message = "请先进行登陆";
        log.warn(message, ex);
        return create(HttpStatus.UNAUTHORIZED, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleServiceException(ServiceException e) {
        String message = e.getMessage();
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<Void> handleException(Exception e) {
        String message = StrUtil.emptyToDefault(e.getMessage(), "系统繁忙, 请稍后");
        log.error("服务异常ID: [{}]", IdUtil.randomUUID(), e);
        return create(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "入参数据格式错误";
        log.warn(message, ex);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleNullPointerException(NullPointerException e) {
        String message = "空指针异常";
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(value = {UnsupportedOperationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleUnsupportedOperationException(UnsupportedOperationException e) {
        String message = "操作不支持";
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = StrUtil.emptyToDefault(e.getMessage(), "参数错误");
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResult<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = handleValidException(e.getBindingResult());
        log.warn(message, e);
        return create(HttpStatus.BAD_REQUEST, message);
    }

    @SendToUser(destinations = StringConstants.ERROR_DEST, broadcast = false)
    @MessageExceptionHandler(value = {BindException.class})
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
