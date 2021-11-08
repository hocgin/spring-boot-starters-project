package in.hocg.boot.youtube.sample.service;

import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.boot.youtube.sample.BootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2021/10/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class YoutubeServiceTest extends AbstractSpringBootTest {
    @Autowired
    YoutubeService youtubeService;

    public void before() {
    }

    @org.junit.Test
    public void getList() {
        youtubeService.getList();
    }

    @org.junit.Test
    public void uploadLocal() {
        // 2m15s - 17m15s
        youtubeService.uploadLocal("", "/Users/hocgin/Downloads/动漫视频/一念永恒(第一季)");
    }
}
