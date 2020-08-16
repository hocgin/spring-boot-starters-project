package in.hocg.boot.logging.autoconfiguration;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.core.env.Environment;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Plugin(name = "logging", category = StrLookup.CATEGORY)
public class LoggingLookup extends AbstractLookup implements StrLookup {
    @Getter
    @Setter
    private static Environment environment;
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");

    @Override
    public String lookup(LogEvent logEvent, String s) {
        try {
            return environment.getProperty(s);
        } catch (final Exception e) {
            LOGGER.warn(LOOKUP, "Error while getting system property [{}].", s, e);
            return null;
        }
    }
}
