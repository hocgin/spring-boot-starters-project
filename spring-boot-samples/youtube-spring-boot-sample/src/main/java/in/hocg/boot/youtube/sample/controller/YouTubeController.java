package in.hocg.boot.youtube.sample.controller;

import in.hocg.boot.web.result.Result;
import in.hocg.boot.youtube.sample.constants.Constants;
import in.hocg.boot.youtube.sample.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hocgin on 2021/10/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class YouTubeController {
    private final YoutubeService youtubeService;

    @ResponseBody
    @GetMapping("/upload/local")
    public Result<Void> uploadLocal(@RequestParam(value = "clientId", required = false, defaultValue = Constants.DEFAULT_CLIENT_ID) String clientId,
                                    @RequestParam(value = "dir", required = false, defaultValue = "/Users/hocgin/Downloads/动漫视频/一念永恒(第一季)") String dir) {
        youtubeService.uploadLocal(clientId, dir);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/channels")
    public Result<?> list(@RequestParam(value = "clientId", required = false, defaultValue = Constants.DEFAULT_CLIENT_ID) String clientId) {
        return Result.success(youtubeService.channels(clientId));
    }

    @ResponseBody
    @GetMapping("/playlist")
    public Result<?> playlist(@RequestParam(value = "clientId", required = false, defaultValue = Constants.DEFAULT_CLIENT_ID) String clientId) {
        return Result.success(youtubeService.playlists(clientId));
    }

}
