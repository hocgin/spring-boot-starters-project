package in.hocg.boot.web.sample;

import in.hocg.boot.web.servlet.ServletConfiguration;
import in.hocg.boot.web.webflux.WebFluxConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SpringBootTest(classes = TestConfiguration.class,
    properties = "spring.main.web-application-type=reactive")
public class WebfluxTests {
    @Autowired(required = false)
    ServletConfiguration servletConfiguration;
    @Autowired(required = false)
    WebFluxConfiguration webFluxConfiguration;

    @Test
    public void testWebflux() {
        Assert.assertNull(servletConfiguration);
        Assert.assertNotNull(webFluxConfiguration);
    }
}
