package in.hocg.boot.web.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2021/7/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LazyController {
    private final LazyService service;

    @GetMapping("/worked")
    public String worked() {
        return service.worked();
    }
}
