package in.hocg.boot.oss.autoconfigure.controller;

import in.hocg.boot.oss.autoconfigure.impl.LocalFileBerviceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class LocalOssController {
    private final LocalFileBerviceImpl localFileBervice;

    @SneakyThrows
    @GetMapping("/oss/{dirname}/{filename}")
    public ResponseEntity<?> showFile(@PathVariable("dirname") String dirname, @PathVariable("filename") String filename) {
        File file;
        try {
            file = localFileBervice.getFile(dirname, filename);
        } catch (Exception e) {
            log.warn("get file error", e);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
            .headers(new HttpHeaders() {{
                setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
                setCacheControl(CacheControl.noCache());
                setPragma("no-cache");
                setExpires(0L);
            }})
            .contentLength(file.length())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new InputStreamResource(new FileInputStream(file)));
    }

}
