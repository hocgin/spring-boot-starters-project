package in.hocg.boot.youtube.sample.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.collect.Lists;
import in.hocg.boot.web.autoconfiguration.properties.BootProperties;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBervice;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeHelper;
import in.hocg.boot.youtube.autoconfiguration.utils.YoutubeUtils;
import in.hocg.boot.youtube.autoconfiguration.utils.data.CredentialChannel;
import in.hocg.boot.youtube.sample.constants.Constants;
import in.hocg.boot.youtube.sample.controller.YouTubeController;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2021/10/5
 * email: hocgin@gmail.com
 * - https://developers-dot-devsite-v2-prod.appspot.com/youtube/v3/docs/playlists/insert
 *
 * @author hocgin
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class YoutubeService {
    private final YoutubeBervice youtubeBervice;
    private final BootProperties bootProperties;

    @SneakyThrows
    public void getList() {
        YouTube youtube = YoutubeUtils.create();
        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setKey("AIzaSyAlTHprB8LKCr8YoAJTiNZ2R--2ZPSumy0");

        SearchListResponse response = search.execute();
        log.debug("=> {}", response);
    }

    @SneakyThrows
    public void getVideo() {
        YouTube youtube = YoutubeUtils.create();
        YouTube.Channels.List search = youtube.channels().list("id,snippet");
        search.setKey("AIzaSyAlTHprB8LKCr8YoAJTiNZ2R--2ZPSumy0");
        ChannelListResponse response = search.execute();
        log.debug("=> {}", response);
    }

    @SneakyThrows
    public void uploadLocal(String clientId, String dir) {
        dir = "/Users/hocgin/Downloads/动漫视频/一念永恒(第一季)_convert";
        File thumbnailFile = new File("/Users/hocgin/Downloads/动漫视频/logo.png");
        String description = "沙雕修仙番｜《一念永恒》动画改编自网络文学作家耳根同名小说，平凡少年白小纯为追求长生之法，一次次点燃仙香召唤仙人却屡遭雷劈。直到引路人李青候掌座的出现…，承包你这个夏天的笑点！";
        ArrayList<String> tags = Lists.newArrayList("中国", "动漫", "玄幻", "China", "Animation");
        String channelId = "UCEAVb3QTuUD6kpvvDq6N_NQ";
        String language = "ZH-CN";
        // 视频类别: https://developers-dot-devsite-v2-prod.appspot.com/youtube/v3/docs/videoCategories/list
        String categoryId = "1";
        // 播放列表
        String playlistId = "PLCEcFGOrM-f83PBJrLUbqtRz7m3WgDkOy";

        File videoDir = Paths.get(dir).toFile();
        YouTube youtube = youtubeBervice.youtube(clientId, YouTubeController.getUserId(), Constants.DEFAULT_SCOPES);
        for (File file : Objects.requireNonNull(videoDir.listFiles())) {
            String name = FileUtil.getPrefix(file);

            Video video = YoutubeHelper.createVideo();
            video.setSnippet(YoutubeHelper.createVideoSnippet(name, description)
                .setDefaultAudioLanguage(language)
                .setCategoryId(categoryId)
                .setDefaultLanguage(language)
                .setChannelId(channelId)
                .setTags(tags));
            InputStreamContent mediaContent = new InputStreamContent("video/*", FileUtil.getInputStream(file));

            // 上传标记
            String parts = StrUtil.join(",", "snippet", "statistics", "status");
            Video rtnVideo = youtube.videos().insert(parts, video, mediaContent).setNotifySubscribers(true).execute();
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + rtnVideo.getId());
            System.out.println("  - Title: " + rtnVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + rtnVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + rtnVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + rtnVideo.getStatistics().getViewCount());

            // 新增到[播放列表]
            String videoId = rtnVideo.getId();
            addPlaylistItem(clientId, playlistId, videoId);

            // 设置图片
            setThumbnail(clientId, videoId, thumbnailFile);
        }
    }

    /**
     * 添加到播放列表
     *
     * @param clientId
     * @param playlistId
     * @param videoId
     */
    @SneakyThrows
    private void addPlaylistItem(String clientId, String playlistId, String videoId) {
        YouTube youtube = youtubeBervice.youtube(clientId, YouTubeController.getUserId(), Constants.DEFAULT_SCOPES);
        ResourceId resourceId = new ResourceId();
        resourceId.setKind("youtube#video");
        resourceId.setVideoId(videoId);

        PlaylistItemSnippet snippet = new PlaylistItemSnippet();
        snippet.setPlaylistId(playlistId);
        snippet.setResourceId(resourceId);

        PlaylistItem playlistItem = new PlaylistItem();
        playlistItem.setSnippet(snippet);

        YouTube.PlaylistItems.Insert insertRo = youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
        PlaylistItem rtnInsert = insertRo.execute();

        System.out.println("New PlaylistItem name: " + rtnInsert.getSnippet().getTitle());
        System.out.println(" - Video id: " + rtnInsert.getSnippet().getResourceId().getVideoId());
        System.out.println(" - Posted: " + rtnInsert.getSnippet().getPublishedAt());
        System.out.println(" - Channel: " + rtnInsert.getSnippet().getChannelId());
        // rtnInsert.getId();
    }

    /**
     * 设置视频的图片
     *
     * @param clientId
     * @param videoId
     * @param imageFile
     */
    @SneakyThrows
    private void setThumbnail(String clientId, String videoId, File imageFile) {
        YouTube youtube = youtubeBervice.youtube(clientId, YouTubeController.getUserId(), Constants.DEFAULT_SCOPES);
        InputStreamContent mediaContent = new InputStreamContent("image/png", new BufferedInputStream(new FileInputStream(imageFile)));
        mediaContent.setLength(imageFile.length());
        mediaContent.setLength(imageFile.length());
        YouTube.Thumbnails.Set thumbnailSet = youtube.thumbnails().set(videoId, mediaContent);
        MediaHttpUploader uploader = thumbnailSet.getMediaHttpUploader();
        uploader.setDirectUploadEnabled(false);
        ThumbnailSetResponse response = thumbnailSet.execute();
        // Print the URL for the updated video's thumbnail image.
        System.out.println("\n================== Uploaded Thumbnail ==================\n");
        System.out.println("  - Url: " + response.getItems().get(0).getDefault().getUrl());
    }

    @SneakyThrows
    public List<?> channels(String clientId) {
        YouTube youtube = youtubeBervice.youtube(clientId, YouTubeController.getUserId(), Constants.DEFAULT_SCOPES);
        String part = StrUtil.join(",", "id", "contentDetails");
        return youtube.channels().list(part).setMine(true).execute().getItems();
    }

    @SneakyThrows
    public List<?> playlists(String clientId) {
        YouTube youtube = youtubeBervice.youtube(clientId, YouTubeController.getUserId(), Constants.DEFAULT_SCOPES);
        String part = StrUtil.join(",", "id", "contentDetails");
        return youtube.playlists().list(part).setMine(true).execute().getItems();
    }

    public String authorize(String clientId, List<String> scopes) {
        String hostname = bootProperties.getHostname();
        String re = StrUtil.format("{}/youtube/{}/callback", hostname, clientId);
        return youtubeBervice.authorize(clientId, re, scopes);
    }

    public void authorizeCallback(String clientId, List<String> scopes, String code) {
        String hostname = bootProperties.getHostname();
        String re = StrUtil.format("{}/youtube/{}/callback", hostname, clientId);
        CredentialChannel credential = youtubeBervice.getCredential(clientId, YouTubeController.getUserId(), re, scopes, code);
        log.debug("credential: {}", credential);
    }

    public Boolean refresh(String clientId) {
        Credential credential = youtubeBervice.loadCredential(clientId, YouTubeController.getUserId()).orElseThrow();
        boolean verifyToken = youtubeBervice.validToken(credential);
        if (!verifyToken) {
            credential = youtubeBervice.refreshToken(credential);
        }
        log.debug("credential: {}", credential);
        return true;
    }

    public List<?> listCredentials(String clientId) {
        return youtubeBervice.listCredentials(clientId);
    }
}
