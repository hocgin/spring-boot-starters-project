package in.hocg.boot.message.sample.controller;

import in.hocg.boot.message.sample.service.TransactionalService;
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
public class TransactionalController {
    private final TransactionalService service;

    @GetMapping("/prepare")
    public void transactionalPrepare() {
        service.transactionalPrepare();
    }

    @GetMapping("/prepare2")
    public void transactionalPrepare2() {
        service.transactionalPrepare2();
    }
}
