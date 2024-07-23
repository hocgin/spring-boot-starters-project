package in.hocg.boot.geo;

import in.hocg.boot.geoip.autoconfiguration.GeoIpLocation;
import in.hocg.boot.geoip.autoconfiguration.core.GeoIpBervice;
import in.hocg.boot.geoip.sample.BootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2024/06/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeoTests {
    @Autowired
    GeoIpBervice geoIpBervice;

    @Test
    public void testGeo() {
        GeoIpLocation location = geoIpBervice.getLocationByIP("103.98.19.182");
        log.info("location = {}", location);
    }
}
