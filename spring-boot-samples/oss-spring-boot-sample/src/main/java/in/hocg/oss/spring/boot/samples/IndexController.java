package in.hocg.oss.spring.boot.samples;

import in.hocg.boot.oss.autoconfigure.core.OssFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hocgin on 2019/6/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class IndexController {
    private final OssFileService ossFileService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(ossFileService.upload(file));
    }

}
