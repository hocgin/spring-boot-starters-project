package in.hocg.boot.youtube.sample.controller;

import cn.hutool.core.util.StrUtil;
import com.google.api.client.util.Lists;
import in.hocg.boot.web.autoconfiguration.properties.BootProperties;
import in.hocg.boot.utils.struct.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hocgin on 2021/10/5
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Controller
@RequestMapping
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class IndexController {
    private final BootProperties bootProperties;

    @ResponseBody
    @GetMapping
    public Result<List<String>> index() {
        String hostname = bootProperties.getHostname();

        List<String> result = Lists.newArrayList();
        result.add(StrUtil.format("{}/youtube/authorize", hostname));
        // id,status,topicDetails,snippet,contentDetails,statistics
        result.add(StrUtil.format("{}/channels", hostname));
        result.add(StrUtil.format("{}/upload/local", hostname));
        // 清除已授权应用
        result.add("https://myaccount.google.com/u/2/permissions");
        // 授权用户
        result.add("https://console.cloud.google.com/apis/credentials/consent?project=fluent-tea-239712");
        // API 接口文档
        result.add("https://developers.google.com/youtube/v3/docs/videos");
        // 配额限制(每日只有 10000 额度)
        result.add("https://developers.google.com/youtube/v3/determine_quota_cost");
        // 官方案例
        result.add("https://github.com/youtube/api-samples");
        return Result.success(result);
    }
}
