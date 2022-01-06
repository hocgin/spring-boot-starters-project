package in.hocg.boot.test.autoconfiguration.core;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author hocgin
 */
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractSpringBootTest {
    @LocalServerPort
    protected Integer port;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected TestRestTemplate testRestTemplate;


}
