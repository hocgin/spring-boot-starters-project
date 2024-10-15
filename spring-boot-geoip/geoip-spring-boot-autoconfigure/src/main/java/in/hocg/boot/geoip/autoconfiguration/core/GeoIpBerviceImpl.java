package in.hocg.boot.geoip.autoconfiguration.core;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import in.hocg.boot.geoip.autoconfiguration.GeoIpLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

/**
 * Created by hocgin on 2024/06/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class GeoIpBerviceImpl implements GeoIpBervice {
    public GeoIpBerviceImpl(@Autowired(required = false) DatabaseReader countryReader,
                            @Autowired(required = false) DatabaseReader cityReader) {
        this.countryReader = countryReader;
        this.cityReader = cityReader;
    }

    private final DatabaseReader countryReader;
    private final DatabaseReader cityReader;

    @Override

    public Optional<String> getCountryByIP(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = countryReader.country(ipAddress);
            Country country = response.getCountry();
            return Optional.ofNullable(country.getIsoCode());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * 获取ip地址映射的国家
     *
     * @param ipAddress
     * @return
     */
    @Override
    public GeoIpLocation getLocationByIP(String ipAddress) {
        GeoIpLocation geoLocation = null;
        if (null == cityReader) {
            //System.err.println("location database is not found.");
            log.error("location database is not found.");
        } else {
            try {
                geoLocation = new GeoIpLocation();
                InetAddress ipAdd = InetAddress.getByName(ipAddress);
                CityResponse response = cityReader.city(ipAdd);
                Country country = response.getCountry();
                geoLocation.setCountryCode(country.getIsoCode());
                geoLocation.setCountryName(country.getName());
                Subdivision subdivision = response.getMostSpecificSubdivision();
                geoLocation.setRegionName(subdivision.getName());
                City city = response.getCity();
                geoLocation.setCity(city.getName());
                Postal postal = response.getPostal();
                geoLocation.setPostalCode(postal.getCode());
                geoLocation.setLatitude(String.valueOf(response.getLocation().getLatitude()));
                geoLocation.setLongitude(String.valueOf(response.getLocation().getLongitude()));
            } catch (GeoIp2Exception e) {
                log.error("{} 解析地址异常", getClass(), e);
                try {
                    if (!String.valueOf(e.getMessage()).startsWith("The address 10.10.") && !String.valueOf(e.getMessage()).startsWith("The address 192.168.")) {
                        log.error(e.getMessage());
                    }
                } catch (Exception ex) {
                    log.error(e.getMessage());
                }
                return null;
            } catch (Exception e) {
                log.warn("{} 未找到地址，IP = {}, 实例是否加载 = {}", getClass(), ipAddress, cityReader != null, e);
                return null;
            }
        }
        return geoLocation;
    }

}
