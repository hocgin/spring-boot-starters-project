package in.hocg.boot.youtube.autoconfiguration.core;

import com.google.api.services.youtube.model.*;
import lombok.experimental.UtilityClass;

/**
 * Created by hocgin on 2021/10/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class YoutubeHelper {

    public Video createVideo() {
        Video video = new Video();
        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");
        video.setStatus(status);
        return video;
    }

    /**
     * 图片
     *
     * @param url
     * @return
     */
    public ThumbnailDetails createThumbnailDetails(String url) {
        ThumbnailDetails result = new ThumbnailDetails();
        result.setDefault(new Thumbnail().setUrl(url));
        return result;
    }

    public VideoSnippet createVideoSnippet(String title, String description) {
        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription(description);
        return snippet;
    }

}
