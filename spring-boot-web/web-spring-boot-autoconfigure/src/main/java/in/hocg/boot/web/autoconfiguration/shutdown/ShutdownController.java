package in.hocg.boot.web.autoconfiguration.shutdown;

import in.hocg.boot.web.autoconfiguration.SpringContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

/**
 * Created by hocgin on 2024/08/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
public class ShutdownController {

    @PostMapping("/boot/shutdown")
    public ResponseEntity<Boolean> shutdown(@RequestParam(value = "duration", defaultValue = "30") Long duration,
                                            HttpServletRequest request) {
        if (!request.getServerName().equalsIgnoreCase("localhost")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        new Thread(() -> SpringContext.shutdown(Duration.ofSeconds(duration))).start();
        return ResponseEntity.ok(true);
    }
}
