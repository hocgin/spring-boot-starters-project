package in.hocg.xxljob.spring.boot.samples;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2019/6/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class IndexController {

}
