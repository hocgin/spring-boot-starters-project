package in.hocg.boot.web.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;

/**
 * Created by hocgin on 2021/7/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LazyService {
    private final Writer1 writer;

    public String worked() {
        return writer.write("Hi");
    }
}
