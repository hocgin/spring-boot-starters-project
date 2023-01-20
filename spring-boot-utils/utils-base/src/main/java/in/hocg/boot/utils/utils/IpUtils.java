package in.hocg.boot.utils.utils;

import lombok.experimental.UtilityClass;

import java.util.StringJoiner;

/**
 * Created by hocgin on 2021/5/12
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class IpUtils {
    /**
     * ip to Long
     *
     * @param ipStr ip
     * @return l
     */
    public static long ipToLong(String ipStr) {
        String[] ip = ipStr.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }

    /**
     * long to ip
     *
     * @param longIp ip
     * @return l
     */
    public static String longToIp(long longIp) {
        return new StringJoiner(".")
            .add(String.valueOf(longIp >>> 24))
            .add(String.valueOf((longIp & 0x00FFFFFF) >>> 16))
            .add(String.valueOf((longIp & 0x0000FFFF) >>> 8))
            .add(String.valueOf((longIp & 0x000000FF))).toString();
    }

}
