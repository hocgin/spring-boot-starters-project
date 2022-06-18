package in.hocg.boot.mybatis.plus.extensions.webmagic.controller;


import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.context.annotation.Lazy;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * [BOOT] 爬虫采集表 前端控制器
 * </p>
 *
 * @author hocgin
 * @since 2022-06-16
 */
@Api(tags = "[BOOT] 爬虫采集表")
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@RequestMapping("/webmagic")
public class WebmagicController {

}

