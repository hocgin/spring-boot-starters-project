package in.hocg.boot.web.sample;


import in.hocg.boot.web.autoconfiguration.servlet.ServletConfiguration;
import in.hocg.boot.web.autoconfiguration.webflux.WebFluxConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public class ServletTests {
    @Autowired(required = false)
    ServletConfiguration servletConfiguration;
    @Autowired(required = false)
    WebFluxConfiguration webFluxConfiguration;

    @Test
    public void testServlet() {
        Assert.assertNull(webFluxConfiguration);
        Assert.assertNotNull(servletConfiguration);
    }
}
