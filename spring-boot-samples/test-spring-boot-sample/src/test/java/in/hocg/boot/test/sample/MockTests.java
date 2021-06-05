package in.hocg.boot.test.sample;

import in.hocg.boot.test.autoconfiguration.core.AbstractSpringBootTest;
import in.hocg.boot.test.sample.controller.IndexController;
import in.hocg.boot.test.sample.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hocgin on 2021/3/30
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockTests extends AbstractSpringBootTest {
    @Autowired
    private IndexController indexController;
    @Mock
    private IndexService indexService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//        Object indexServiceRef = ReflectionTestUtils.getField(indexController, "service");
        ReflectionTestUtils.setField(indexController, "service", indexService);
    }

    @Test
    public void mock() {
        when(indexService.worked()).thenReturn("Mock Data");

        Assertions.assertEquals("Mock Data", indexController.worked(""));
    }

    @Test
    public void http() {
        when(indexService.worked()).thenReturn("Mock Data");

        ResponseEntity<String> response = testRestTemplate.getForEntity("/index/worked", String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Mock Data", response.getBody());
    }

    @Test
    public void http2() throws Exception {
        when(indexService.worked()).thenReturn("Mock Data");

        mockMvc.perform(MockMvcRequestBuilders.get("/index/worked")
            .accept(MediaType.ALL))
            // 进行结果验证
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content()
                .string(CoreMatchers.equalTo("Mock Data"))
            );
    }
}
