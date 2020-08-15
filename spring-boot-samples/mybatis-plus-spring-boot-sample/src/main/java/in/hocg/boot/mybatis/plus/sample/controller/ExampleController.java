package in.hocg.boot.mybatis.plus.sample.controller;

import in.hocg.boot.mybatis.plus.sample.service.ExampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hocgin
 * @since 2020-08-04
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@RequestMapping("/example")
public class ExampleController {
    private final ExampleService service;

    @GetMapping
    public String index() {
        return service.index();
    }
}

