package in.hocg.boot.webmagic.autoconfiguration.extensions;

import cn.hutool.core.util.ClassUtil;
import in.hocg.boot.mybatis.plus.extensions.webmagic.autoconfiguration.WebmagicMybatisPlusExtAutoConfiguration;
import in.hocg.boot.mybatis.plus.extensions.webmagic.enums.Status;
import in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro.CreateWebmagicRo;
import in.hocg.boot.mybatis.plus.extensions.webmagic.service.WebmagicService;
import in.hocg.boot.web.autoconfiguration.utils.SerializableUtils;
import in.hocg.boot.webmagic.autoconfiguration.listener.event.WebmagicEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/6/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Component
@ConditionalOnClass(WebmagicMybatisPlusExtAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class WebmagicMybatisListener {
    private final WebmagicService webmagicService;

    @Async
    @EventListener(classes = WebmagicEvent.class)
    public void handle(WebmagicEvent event) {
        Object source = event.getSource();
        String url = event.getUrl();
        String pullData = SerializableUtils.toStr(source);
        String type = ClassUtil.getClassName(source.getClass(), true);

        CreateWebmagicRo entity = new CreateWebmagicRo();
        entity.setType(type);
        entity.setPullUrl(url);
        entity.setStatus(Status.Done.getCodeStr());
        entity.setFinishedAt(LocalDateTime.now());
        entity.setPullData(pullData);
        webmagicService.create(entity);
    }
}
