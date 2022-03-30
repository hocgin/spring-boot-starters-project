package in.hocg.boot.youtube.autoconfiguration.core;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.services.youtube.YouTube;
import in.hocg.boot.utils.function.BiConsumerThrow;
import in.hocg.boot.youtube.autoconfiguration.core.datastore.DataCredential;
import in.hocg.boot.youtube.autoconfiguration.exception.TokenUnusedException;
import in.hocg.boot.youtube.autoconfiguration.properties.YoutubeProperties;
import in.hocg.boot.youtube.autoconfiguration.utils.YoutubeUtils;
import lombok.SneakyThrows;

import java.util.*;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface YoutubeBervice {

    default GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String clientId, List<String> scopes) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.getAuthorizationCodeFlow(clientConfig.getClientId(), clientConfig.getClientSecret(), scopes);
    }

    List<DataCredential> listCredentials(String clientId);

    /**
     * 校验token
     *
     * @param credential
     * @return
     */
    default boolean validToken(Credential credential) {
        return Objects.isNull(credential.getExpiresInSeconds())
            || new Date(credential.getExpiresInSeconds()).after(new Date());
    }

    @SneakyThrows
    default Credential refreshToken(Credential credential) {
        credential.refreshToken();
        return credential;
    }

    default Optional<Credential> loadCredential(String clientId, String userId) {
        return loadCredential(clientId, userId, YoutubeUtils.DEFAULT_SCOPES);
    }

    default Optional<Credential> loadCredential(String clientId, String userId, List<String> scopes) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return Optional.ofNullable(YoutubeUtils.loadCredential(clientConfig.getClientId(), userId, clientConfig.getClientSecret(), scopes));
    }

    default Credential getCredential(String clientId, String userId, String redirectUri, List<String> scopes, String code) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.getCredential(clientConfig.getClientId(), userId, clientConfig.getClientSecret(), redirectUri, scopes, code);
    }

    /**
     * 发起授权
     *
     * @param clientId
     * @param redirectUri
     * @param scopes
     * @return
     */
    default String authorize(String clientId, String redirectUri, List<String> scopes) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        return YoutubeUtils.authorize(clientConfig.getClientId(), clientConfig.getClientSecret(), redirectUri, scopes);
    }

    default void youtube(String clientId, String userId, List<String> scopes, BiConsumerThrow<YoutubeProperties.ClientConfig, YouTube> consumer) {
        YoutubeProperties.ClientConfig clientConfig = getClientConfig(clientId);
        Credential credential = loadCredential(clientId, userId, scopes).orElseThrow(() -> new TokenUnusedException("Token 失效"));
        YouTube youtube = new YouTube.Builder(YoutubeUtils.HTTP_TRANSPORT, YoutubeUtils.JSON_FACTORY, credential)
            .setApplicationName(clientConfig.getApplicationName()).build();
        try {
            consumer.accept(clientConfig, youtube);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务提供商服务
     *
     * @param clientId
     * @param scopes
     * @return
     */
    default YouTube youtube(String clientId, String userId, List<String> scopes) {
        Credential credential = loadCredential(clientId, userId, scopes).orElseThrow(() -> new TokenUnusedException("Token 失效"));
        return new YouTube.Builder(YoutubeUtils.HTTP_TRANSPORT, YoutubeUtils.JSON_FACTORY, credential).build();
    }

    /**
     * 配置服务提供商
     *
     * @param map
     */
    void setMultiConfigStorages(java.util.Map<java.lang.String, YoutubeProperties.ClientConfig> map);

    /**
     * 获取指定的服务提供商配置
     *
     * @param clientId
     * @return
     */
    YoutubeProperties.ClientConfig getClientConfig(String clientId);

    /**
     * 获取所有的服务提供商配置
     *
     * @return
     */
    Map<String, YoutubeProperties.ClientConfig> getClientConfigs();
}
