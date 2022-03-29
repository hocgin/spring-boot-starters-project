package in.hocg.boot.javacv.autoconfiguration.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import in.hocg.boot.javacv.autoconfiguration.utils.FfmpegHelper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by hocgin on 2021/10/6
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@UtilityClass
public class Commands {

    @SneakyThrows({IOException.class, InterruptedException.class})
    public boolean command(String... args) {
        String command = Joiner.on(" ").join(args);
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        log.debug(command);
        return process.exitValue() == 0;
    }

    /**
     * 剪辑视频
     *
     * @param srcFile
     * @param startAt
     * @param lengthAt
     * @return
     */
    public File sub(File srcFile, LocalTime startAt, LocalTime lengthAt) {
        String prefix = FileUtil.getPrefix(srcFile);
        String suffix = FileUtil.getSuffix(srcFile);

        File outFile = createOutFile(prefix, suffix);
        outFile.deleteOnExit();
        Commands.command(StrUtil.format("{} -y -i {} -ss {} -t {} -codec copy {}", FfmpegHelper.ffmpeg(),
            srcFile.getPath(),
            startAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
            lengthAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
            outFile.getPath()));
        return outFile;
    }

    private File createOutFile(String prefix, String suffix) {
        File tempDir = FileUtil.mkdir(Files.createTempDir());
        return FileUtil.createTempFile(prefix + ".", "." + suffix, tempDir, true);
    }

    /**
     * 截取指定时间的图片
     *
     * @param srcFile
     * @param fragmentAt
     * @param width
     * @param height
     * @return
     */
    public File fragment(File srcFile, LocalTime fragmentAt, Integer width, Integer height) {
        String suffix = "jpeg";
        File outFile = createOutFile(FileUtil.getPrefix(srcFile), suffix);
        String command = StrUtil.format("{} -y -i {} -vframes 1 -ss {} -f m{} -s {} -an {}",
            FfmpegHelper.ffmpeg(),
            srcFile.getPath(),
            fragmentAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
            suffix,
            width + "*" + height,
            outFile.getPath());
        Commands.command(command);
        return outFile;
    }

    /**
     * 截取 Gif
     *
     * @param srcFile
     * @param startAt
     * @param lengthAt
     * @return
     */
    public File gif(File srcFile, LocalTime startAt, LocalTime lengthAt) {
        File outFile = createOutFile(FileUtil.getPrefix(srcFile), "gif");
        String command = StrUtil.format("{} -ss {} -t {} -i {} {}",
            FfmpegHelper.ffmpeg(),
            srcFile.getPath(),
            startAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
            lengthAt.format(DateTimeFormatter.ISO_LOCAL_TIME),
            outFile.getPath());
        Commands.command(command);
        return outFile;
    }

    /**
     * 合并视频
     *
     * @param first
     * @param files
     * @return
     */
    public static File merge(File first, File... files) {
        File outFile = createOutFile(FileUtil.getPrefix(first) + "_merge", FileUtil.getSuffix(first));
        List<File> allFiles = Lists.newArrayList();
        allFiles.add(first);
        allFiles.addAll(Lists.newArrayList(files));

        String str = allFiles.stream().map(file -> StrUtil.format("-i \"{}\"", file.getPath()))
            .reduce((s, s2) -> s + " " + s2).orElse("");

        String command = StrUtil.format("{} {} -c:v copy -c:a aac -strict experimental {}",
            FfmpegHelper.ffmpeg(),
            str,
            outFile.getPath());
        Commands.command(command);
        return outFile;
    }
}
