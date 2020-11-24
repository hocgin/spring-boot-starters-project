package in.hocg.boot.sso.client.autoconfigure.core.servlet;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * Created by hocgin on 2018/8/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class ResponseUtils {

    /**
     * 指定输出 UTF-8
     *
     * @param response
     * @return
     */
    public HttpServletResponse setUtf8(ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return httpServletResponse;
    }

}
