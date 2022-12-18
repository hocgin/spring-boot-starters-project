package in.hocg.boot.web.autoconfiguration.utils;

import cn.hutool.core.util.StrUtil;
import in.hocg.boot.web.autoconfiguration.utils.web.ResponseUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by hocgin on 2022/12/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class WebUtils {
    /**
     * 获取客户端真实IP
     *
     * @param request
     * @return
     */
    public String getClientIp(HttpServletRequest request) {
        String ip;
        String forwardedFor = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        if (StrUtil.isNotBlank(forwardedFor)
            && !"unknown".equalsIgnoreCase(forwardedFor)) {
            int index = forwardedFor.indexOf(",");
            if (index != -1) {
                ip = forwardedFor.substring(0, index);
            } else {
                ip = forwardedFor;
            }
        } else if (StrUtil.isNotBlank(realIp)
            && !"unknown".equalsIgnoreCase(realIp)) {
            ip = realIp;
        } else {
            ip = request.getRemoteAddr();
        }

        // 本地名单
        if (Arrays.asList(new String[]{"0:0:0:0:0:0:0:1", "127.0.0.1"}).contains(ip)) {
            return "110.80.68.212";
        }
        return ip;
    }

    /**
     * 获取 User-Agent
     * User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36
     *
     * @param request
     * @return
     */
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public String getHost(HttpServletRequest request) {
        return request.getHeader("Host");
    }

    public boolean isAJAX(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }


    public static ResponseEntity<Void> found(String url) {
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(url))
            .build();
    }

    public static ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static <T> ResponseEntity<?> download(T t) {
        return ResponseUtils.download(t, "unknown");
    }

    public static <T> ResponseEntity<?> preview(T t) {
        return ResponseUtils.preview(t, null);
    }

    public static <T> ResponseEntity<?> preview(T t, MediaType mediaType) {
        Resource resource = WebUtils.asResource(t);

        return ResponseEntity
            .ok()
            .headers(new HttpHeaders() {{
                setContentDisposition(ContentDisposition.builder("inline").build());
                setCacheControl(CacheControl.noCache());
                setPragma("no-cache");
                setExpires(0L);
            }})
            .contentType(mediaType)
            .body(resource);
    }

    public static <T> ResponseEntity<?> download(T t, String filename) {
        Resource resource = WebUtils.asResource(t);

        return ResponseEntity
            .ok()
            .headers(new HttpHeaders() {{
                setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
                setCacheControl(CacheControl.noCache());
                setPragma("no-cache");
                setExpires(0L);
            }})
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    private static <T> Resource asResource(T t) {
        Resource resource;
        if (t instanceof InputStream) {
            resource = new InputStreamResource((InputStream) t);
        } else if (t instanceof Resource) {
            resource = (Resource) t;
        } else if (t instanceof File) {
            resource = new FileSystemResource((File) t);
        } else if (t instanceof URI || t instanceof String) {
            URI uri;
            if (t instanceof String) {
                uri = URI.create(((String) t));
            } else {
                uri = (URI) t;
            }
            try {
                resource = new InputStreamResource(uri.toURL().openStream());
            } catch (IOException e) {
                throw new IllegalStateException("读取链接异常");
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return resource;
    }
}
