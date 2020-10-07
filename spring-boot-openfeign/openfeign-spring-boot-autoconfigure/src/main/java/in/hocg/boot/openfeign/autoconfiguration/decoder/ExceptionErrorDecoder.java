package in.hocg.boot.openfeign.autoconfiguration.decoder;

import cn.hutool.json.JSONUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;

/**
 * Created by hocgin on 2020/10/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class ExceptionErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        Exception exception = null;
        try {
            String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
            FeignExceptionInfo result = JSONUtil.toBean(json, FeignExceptionInfo.class);
            exception = asException(result);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return exception;
    }

    private Exception asException(FeignExceptionInfo result) {
        String exception = result.getException();
        String message = result.getMessage();
        Exception ex;
        try {
            Class<?> clazz = Class.forName(exception);
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            ex = (Exception) constructor.newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException(message);
        }
        return ex;
    }
}
