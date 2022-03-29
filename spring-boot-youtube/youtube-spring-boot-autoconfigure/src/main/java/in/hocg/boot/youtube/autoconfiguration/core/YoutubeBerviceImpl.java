package in.hocg.boot.youtube.autoconfiguration.core;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStoreFactory;
import in.hocg.boot.youtube.autoconfiguration.core.datastore.DataCredential;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import in.hocg.boot.youtube.autoconfiguration.utils.YoutubeUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class YoutubeBerviceImpl implements YoutubeBervice {
    @Autowired(required = false)
    @Lazy
    private DataStoreFactory dataStoreFactory;
    private Map<String, YoutubeProperties.ClientConfig> configMap = Collections.emptyMap();

    @Override
    @SneakyThrows
    public List<DataCredential> listCredentials(String clientId) {
        return dataStoreFactory.getDataStore(YoutubeUtils.DB_NAME).values().stream()
            .map(credential -> {
                if (credential instanceof StoredCredential) {
                    StoredCredential storedCredential = (StoredCredential) credential;
                    return new DataCredential()
                        .setAccessToken(storedCredential.getAccessToken())
                        .setRefreshToken(storedCredential.getRefreshToken())
                        .setExpirationTimeMilliseconds(storedCredential.getExpirationTimeMilliseconds());
                }
                throw new UnsupportedOperationException();
            }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void setMultiConfigStorages(Map<String, YoutubeProperties.ClientConfig> map) {
        configMap = map;
    }

    @Override
    public YoutubeProperties.ClientConfig getClientConfig(String clientId) {
        return configMap.get(clientId);
    }

    @Override
    public Map<String, YoutubeProperties.ClientConfig> getClientConfigs() {
        return configMap;
    }

}
