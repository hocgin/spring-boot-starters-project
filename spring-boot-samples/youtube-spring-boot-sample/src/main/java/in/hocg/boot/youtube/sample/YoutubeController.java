package in.hocg.boot.youtube.sample;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;
import in.hocg.boot.youtube.autoconfiguration.core.YoutubeBervice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
@RequestMapping("/ytb")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class YoutubeController {
    private final YoutubeBervice youtubeBootService;

    @ResponseBody
    @GetMapping("/link")
    public String link(@RequestParam("clientId") String clientId,
                       @RequestParam("scopes") List<String> scopes) {
        scopes.add("https://www.googleapis.com/auth/youtube");

        String re = StrUtil.format("http://127.0.0.1:8080/ytb/{}/callback", clientId);
        return youtubeBootService.authorize(clientId, re, scopes);
    }

    @ResponseBody
    @GetMapping("/{clientId}/callback")
    public Object callback(@PathVariable String clientId,
                           @RequestParam("code") String code, @RequestParam("scope") List<String> scopes) {
        String re = StrUtil.format("http://127.0.0.1:8080/ytb/{}/callback", clientId);
        Credential credential = youtubeBootService.getCredential(clientId, re, scopes, code);
        return credential.getAccessToken();
    }


    @ResponseBody
    @GetMapping("/{clientId}/work")
    public Object work(@PathVariable String clientId) {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        youtubeBootService.youtube(clientId, scopes, (clientConfig, youTube) -> {
            YouTube.Channels.List contentDetails = youTube.channels().list("contentDetails");
            contentDetails.setMine(true);
            contentDetails.setFields("items/contentDetails");
            ChannelListResponse response = contentDetails.execute();
            System.out.println(response);


            // 上传视频
            Video video = new Video();
            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            video.setStatus(status);
            VideoSnippet snippet = new VideoSnippet();

            Calendar cal = Calendar.getInstance();
            snippet.setTitle("Test Upload via Java on " + cal.getTime());
            snippet.setDescription(
                "Video uploaded via YouTube Data API V3 using the Java library " + "on " + cal.getTime());

            InputStreamContent mediaContent = new InputStreamContent("video/*",
                FileUtil.getInputStream("/Users/hocgin/Projects/api-samples/java/src/main/resources/sample-video.mp4"));
            // Set the keyword tags that you want to associate with the video.
            List<String> tags = new ArrayList<>();
            tags.add("test");
            tags.add("example");
            tags.add("java");
            tags.add("YouTube Data API V3");
            tags.add("erase me");
            snippet.setTags(tags);

            video.setSnippet(snippet);
            YouTube.Videos.Insert videoInsert = youTube.videos().insert("snippet,statistics,status", video, mediaContent);
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
            uploader.setDirectUploadEnabled(false);

            MediaHttpUploaderProgressListener progressListener = uploader1 -> {
                switch (uploader1.getUploadState()) {
                    case INITIATION_STARTED:
                        System.out.println("Initiation Started");
                        break;
                    case INITIATION_COMPLETE:
                        System.out.println("Initiation Completed");
                        break;
                    case MEDIA_IN_PROGRESS:
                        System.out.println("Upload in progress");
                        System.out.println("Upload percentage: " + uploader1.getProgress());
                        break;
                    case MEDIA_COMPLETE:
                        System.out.println("Upload Completed!");
                        break;
                    case NOT_STARTED:
                        System.out.println("Upload Not Started!");
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            };
            uploader.setProgressListener(progressListener);
            Video returnedVideo = videoInsert.execute();
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
        });
        return null;
    }
}
