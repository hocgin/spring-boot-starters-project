package in.hocg.boot.changelog.sample.controller;

import in.hocg.boot.changelog.sample.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping("/transactional")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class IndexController {
    private final IndexService service;

}
