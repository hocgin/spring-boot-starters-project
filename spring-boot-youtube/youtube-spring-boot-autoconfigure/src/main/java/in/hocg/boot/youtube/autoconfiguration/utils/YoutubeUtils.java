package in.hocg.boot.youtube.autoconfiguration.utils;

import cn.hutool.core.util.StrUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import in.hocg.boot.web.autoconfiguration.SpringContext;
import in.hocg.boot.youtube.autoconfiguration.utils.data.CredentialChannel;
import in.hocg.boot.youtube.autoconfiguration.utils.data.YouTubeChannel;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class YoutubeUtils {
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String DB_NAME = "DB";
    public static final List<String> DEFAULT_SCOPES = List.of("https://www.googleapis.com/auth/youtube");

    /**
     * 发起授权
     *
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @return
     */
    public static String authorize(String clientId, String clientSecret, String redirectUri, List<String> scopes) {
        GoogleAuthorizationCodeFlow flow = getAuthorizationCodeFlow(clientId, clientSecret, scopes);
        return flow.newAuthorizationUrl().setRedirectUri(redirectUri).setAccessType("offline").build();
    }

    /**
     * 获取凭证
     *
     * @param clientId
     * @param clientSecret
     * @param redirectUri
     * @param scopes
     * @param code
     * @return
     */
    public static CredentialChannel getCredential(String clientId, String clientSecret, String redirectUri, List<String> scopes, String code) {
        return getCredential(clientId, null, clientSecret, redirectUri, scopes, code);
    }

    /**
     * 获取凭证
     *
     * @param clientId
     * @param userId
     * @param clientSecret
     * @param redirectUri
     * @param scopes
     * @param code
     * @return
     */
    @SneakyThrows
    public static CredentialChannel getCredential(String clientId, final String userId, String clientSecret, String redirectUri, List<String> scopes, String code) {
        CredentialChannel result = new CredentialChannel();
        Credential credential2 = getCredential(clientId, clientSecret, redirectUri, scopes, code, credential -> {
            String userId1 = userId;
            if (StrUtil.isBlank(userId1)) {
                YouTubeChannel youTubeChannel = getYouTubeChannel(credential);
                result.setChannel(youTubeChannel);
                userId1 = youTubeChannel.getChannelId();
            }
            result.setUserId(userId1);
            return userId1;
        });
        result.setCredential(credential2);
        return result;
    }


    @SneakyThrows
    public static Credential getCredential(String clientId, String clientSecret, String redirectUri, List<String> scopes, String code,
                                           @NonNull Function<Credential, String> getUserIdFunction) {
        GoogleAuthorizationCodeFlow flow = getAuthorizationCodeFlow(clientId, clientSecret, scopes);
        TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        String userId = getUserIdFunction.apply(getCredentialByFlow(flow, tokenResponse));
        return flow.createAndStoreCredential(tokenResponse, userId);
    }


    public static Credential getCredentialByFlow(GoogleAuthorizationCodeFlow flow, TokenResponse tokenResponse) {
        Credential credential = (new Credential.Builder(flow.getMethod()))
            .setTransport(flow.getTransport())
            .setJsonFactory(flow.getJsonFactory())
            .setTokenServerEncodedUrl(flow.getTokenServerEncodedUrl())
            .setClientAuthentication(flow.getClientAuthentication())
            .setRequestInitializer(flow.getRequestInitializer())
            .setClock(flow.getClock()).build();
        credential.setFromTokenResponse(tokenResponse);
        return credential;
    }

    @SneakyThrows
    public static YouTubeChannel getYouTubeChannel(Credential credential) {
        YouTube youtube = new YouTube.Builder(YoutubeUtils.HTTP_TRANSPORT, YoutubeUtils.JSON_FACTORY, credential).build();
        ChannelListResponse response = youtube.channels()
            .list("id,status,snippet,statistics")
            .setMine(true).execute();

        Channel channel = response.getItems().get(0);
        ChannelSnippet snippet = channel.getSnippet();
        Thumbnail thumbnail = snippet.getThumbnails().getDefault();
        ChannelStatus status = channel.getStatus();
        ChannelStatistics statistics = channel.getStatistics();

        String customUrl = StrUtil.blankToDefault(snippet.getCustomUrl(),
            StrUtil.format("https://www.youtube.com/channel/{}", channel.getId()));

        return new YouTubeChannel()
            .setChannelId(channel.getId())
            .setIsLinked(status.getIsLinked())
            .setLongUploadsStatus(status.getLongUploadsStatus())
            .setPrivacyStatus(status.getPrivacyStatus())
            .setVideoCount(statistics.getVideoCount())
            .setSubscriberCount(statistics.getSubscriberCount())
            .setViewCount(statistics.getViewCount())
            .setHiddenSubscriberCount(statistics.getHiddenSubscriberCount())
            .setPrivacyStatus(status.getPrivacyStatus())
            .setPublishedAt(snippet.getPublishedAt())
            .setImageUrl(thumbnail.getUrl())
            .setUrl(customUrl)
            .setTitle(snippet.getTitle());
    }

    @SneakyThrows
    public static Credential loadCredential(String clientId, String userId, String clientSecret, List<String> scopes) {
        return getAuthorizationCodeFlow(clientId, clientSecret, scopes).loadCredential(userId);
    }

    @SneakyThrows
    public static GoogleAuthorizationCodeFlow getAuthorizationCodeFlow(String clientId, String clientSecret, List<String> scopes) {
        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientId, clientSecret, scopes)
            .setCredentialDataStore(SpringContext.getBean(DataStoreFactory.class).getDataStore(DB_NAME))
            .build();
    }

    /**
     * 创建 YouTube
     *
     * @return
     */
    public static YouTube create() {
        return new YouTube.Builder(YoutubeUtils.HTTP_TRANSPORT, YoutubeUtils.JSON_FACTORY, request -> {
        }).build();
    }
}
