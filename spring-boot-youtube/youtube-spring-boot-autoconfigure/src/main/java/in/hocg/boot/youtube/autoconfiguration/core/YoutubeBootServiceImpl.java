package in.hocg.boot.youtube.autoconfiguration.core;

import cn.hutool.core.lang.Assert;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RequiredArgsConstructor
public class YoutubeBootServiceImpl implements YoutubeBootService {
    @Getter
    @Setter
    private Map<String, YoutubeProperties.ClientConfig> configMap = Collections.emptyMap();

    @Override
    public void setMultiConfigStorages(Map<String, YoutubeProperties.ClientConfig> map) {
        configMap = map;
    }

    @Override
    public YoutubeProperties.ClientConfig getClientConfig(String clientId) {
        return Assert.notNull(configMap.get(clientId));
    }
}
