package in.hocg.boot.geoip.autoconfiguration;

import lombok.Data;

@Data
public class GeoIpLocation {
    private String countryCode;
    private String countryName;
    private String region;
    private String regionName;
    private String city;
    private String postalCode;
    private String latitude;
    private String longitude;
}
