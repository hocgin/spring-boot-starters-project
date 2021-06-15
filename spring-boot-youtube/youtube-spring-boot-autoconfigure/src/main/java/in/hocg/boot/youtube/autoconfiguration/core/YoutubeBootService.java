package in.hocg.boot.youtube.autoconfiguration.core;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.youtube.YouTube;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import in.hocg.boot.youtube.autoconfiguration.utils.YoutubeUtils;
import lombok.SneakyThrows;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface YoutubeBootService {

    default GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String clientId, List<String> scopes) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.getAuthorizationCodeFlow(clientConfig.getClientId(), clientConfig.getClientSecret(), scopes);
    }

    @SneakyThrows
    default Credential getCredential(String clientId, List<String> scopes) {
        return getAuthorizationCodeFlow(clientId, scopes).loadCredential("userid");
    }

    default Credential getCredential(String clientId, String redirectUri, List<String> scopes, String code) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.getCredential(clientConfig.getClientId(), clientConfig.getClientSecret(), redirectUri, scopes, code);
    }

    default String auth(String clientId, String redirectUri, List<String> scopes) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.auth(clientConfig.getClientId(), clientConfig.getClientSecret(), redirectUri, scopes);
    }

    default void youtube(String clientId, List<String> scopes, BiConsumer<YoutubeProperties.ClientConfig, YouTube> consumer) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        YouTube youtube = new YouTube.Builder(YoutubeUtils.HTTP_TRANSPORT, YoutubeUtils.JSON_FACTORY, getCredential(clientId, scopes))
            .setApplicationName(clientConfig.getApplicationName()).build();
        consumer.accept(clientConfig, youtube);
    }

    void setMultiConfigStorages(java.util.Map<java.lang.String, YoutubeProperties.ClientConfig> map);

    YoutubeProperties.ClientConfig getClientConfig(String clientId);
}
