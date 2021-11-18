package in.hocg.boot.youtube.sample.controller;

import in.hocg.boot.web.autoconfiguration.utils.web.ResponseUtils;
import in.hocg.boot.web.result.Result;
import in.hocg.boot.youtube.sample.constants.Constants;
import in.hocg.boot.youtube.sample.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by hocgin on 2021/6/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
@RequestMapping("/youtube")
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuthorizeController {
    private final YoutubeService service;

    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize(@RequestParam(value = "clientId", required = false, defaultValue = Constants.DEFAULT_CLIENT_ID) String clientId,
                                          @RequestParam(value = "scopes", required = false, defaultValue = "") List<String> scopes) {
        scopes.add("https://www.googleapis.com/auth/youtube");
        return ResponseUtils.found(service.authorize(clientId, scopes));
    }

    @ResponseBody
    @GetMapping("/{clientId}/callback")
    public Result<Void> callback(@PathVariable String clientId, @RequestParam("code") String code, @RequestParam("scope") List<String> scopes) {
        service.authorizeCallback(clientId, scopes, code);
        return Result.success();
    }

}
