package in.hocg.boot.ffmpeg.autoconfiguration.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by hocgin on 2021/10/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class FfmpegHelper {

    @SneakyThrows
    public String syncCommand(String... args) {
        List<String> command = Lists.newArrayList(ffmpeg());
        command.addAll(Lists.newArrayList(args));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        InputStream is = process.getInputStream();
        InputStream errorIs = process.getErrorStream();
        process.waitFor();
        boolean isOk = process.exitValue() == 0;
        if (isOk) {
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }
        log.error("命令[{}] 执行失败: {}", processBuilder.command(), StreamUtils.copyToString(errorIs, StandardCharsets.UTF_8));
        throw new IllegalArgumentException("命令执行失败: " + processBuilder.command());
    }

    public static String ffmpeg() {
        OsInfo osInfo = SystemUtil.getOsInfo();
        String os;
        if (osInfo.isMac()) {
            os = "macos";
        } else if (osInfo.isLinux()) {
            os = "linux";
        } else if (osInfo.isWindows()) {
            os = "windows";
        } else {
            throw new UnsupportedOperationException("ffmpeg not support, error reason: unknown os");
        }
        return ClassUtil.getResourceURL(StrUtil.format("app/{}/ffmpeg", os)).getFile();
    }
}
