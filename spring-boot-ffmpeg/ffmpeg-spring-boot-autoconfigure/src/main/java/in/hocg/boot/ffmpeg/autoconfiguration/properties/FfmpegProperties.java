package in.hocg.boot.ffmpeg.autoconfiguration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ConfigurationProperties(FfmpegProperties.PREFIX)
public class FfmpegProperties {
    public static final String PREFIX = "boot.ffmpeg";

}
