package in.hocg.boot.arthas.autoconfiguration;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.arthas.spring.ArthasConfiguration;
import com.taobao.arthas.agent.attach.ArthasAgent;
import in.hocg.boot.arthas.autoconfiguration.properties.ArthasProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@Configuration
@AutoConfigureBefore(ArthasConfiguration.class)
@ConditionalOnProperty(prefix = ArthasProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(ArthasProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ArthasAutoConfiguration {
    @Autowired
    ConfigurableEnvironment environment;

    @Lazy
    @Bean
    public ArthasAgent arthasAgent(@Autowired ArthasProperties arthasProperties) throws Throwable {
        Map<String, Object> arthasConfigMap = BeanUtil.beanToMap(arthasProperties);
        arthasConfigMap.putIfAbsent("disabledCommands", "stop");

        /*
          @see org.springframework.boot.context.ContextIdApplicationContextInitializer#getApplicationId(ConfigurableEnvironment)
         */
        String appName = environment.getProperty("spring.application.name");
        if (arthasConfigMap.get("appName") == null && appName != null) {
            arthasConfigMap.put("appName", appName);
        }

        String agentId = arthasProperties.getAgentId();
        if (StrUtil.isBlank(agentId)) {
            String localhostStr = StrUtil.replace(NetUtil.getLocalhostStr(), ".", "x");
            agentId = StrUtil.format("{}[{}]", StrUtil.blankToDefault(appName, "unknown"), localhostStr);
            arthasConfigMap.put("agentId", agentId);
        }

        // 给配置全加上前缀
        Map<String, String> mapWithPrefix = new HashMap<String, String>(arthasConfigMap.size());
        for (Map.Entry<String, Object> entry : arthasConfigMap.entrySet()) {
            mapWithPrefix.put("arthas." + entry.getKey(), Convert.toStr(entry.getValue()));
        }

        final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
            arthasProperties.isSlientInit(), null);

        arthasAgent.init();
        log.info("Arthas agent start success.");
        return arthasAgent;

    }
}
