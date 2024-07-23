package in.hocg.boot.geoip.autoconfiguration.core;

import in.hocg.boot.geoip.autoconfiguration.GeoIpLocation;

import java.util.Optional;

/**
 * Created by hocgin on 2024/06/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface GeoIpBervice {
    Optional<String> getCountryByIP(String ip);

    GeoIpLocation getLocationByIP(String ipAddress);
}
