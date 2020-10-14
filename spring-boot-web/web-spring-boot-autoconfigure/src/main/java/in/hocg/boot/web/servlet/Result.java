package in.hocg.boot.web.servlet;

import cn.hutool.core.util.URLUtil;
import in.hocg.boot.web.exception.ServiceException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by hocgin on 2020/10/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class Result {

    public static ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static <T> ResponseEntity<?> download(T t, String filename) {
        Resource resource = Result.asResource(t);
        return ResponseEntity.ok()
            .headers(new HttpHeaders() {{
                setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
                setCacheControl(CacheControl.noCache());
                setPragma("no-cache");
                setExpires(0L);
            }})
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    public static <T> ResponseEntity<?> download(T t) {
        return Result.download(t, null);
    }

    public static <T> ResponseEntity<?> preview(T t) {
        return Result.preview(t, null);
    }

    public static <T> ResponseEntity<?> preview(T t, MediaType mediaType) {
        Resource resource = Result.asResource(t);
        return ResponseEntity.ok()
            .headers(new HttpHeaders() {{
                setContentDisposition(ContentDisposition.builder("inline").build());
                setCacheControl(CacheControl.noCache());
                setPragma("no-cache");
                setExpires(0L);
            }})
            .contentType(mediaType)
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
                resource = new InputStreamResource(URLUtil.getStream(uri.toURL()));
            } catch (IOException e) {
                throw ServiceException.wrap("读取链接异常");
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return resource;
    }
}
