package in.hocg.oss;

import in.hocg.boot.oss.autoconfigure.core.OssFileBervice;
import in.hocg.oss.core.AbstractSpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@ActiveProfiles("local")
public class LocalOssFileServiceImplTest extends AbstractSpringBootTest {
    @Autowired
    OssFileBervice ossFileService;

    @Test
    public void testUpload() {
        Assert.assertTrue(true);
        File file = new File("C:\\Users\\admin\\Downloads\\说明.txt");
        String url = ossFileService.upload(file);
        log.info("==> {}", url);
    }
}
