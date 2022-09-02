package in.hocg.boot.oss.autoconfigure.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import in.hocg.boot.oss.autoconfigure.core.OssFileBervice;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class LocalFileBerviceImpl implements OssFileBervice {
    private final String space;
    private final String domain;

    @Override
    public String upload(File file, String filename) {
        return this.upload(file, filename, space);
    }

    public File getFile(String dirname, String filename) {
        return FileUtil.file(getLocalFile(space, dirname), filename);
    }

    @Override
    @SneakyThrows(IOException.class)
    public String upload(InputStream is, String filename, String space) {
        StreamUtils.copy(is, FileUtil.getOutputStream(getLocalFile(space, filename)));
        return StrUtil.format("{}/oss/{}", domain, filename);
    }

    private File getLocalFile(String space, String filename) {
        if (StrUtil.isBlank(space)) {
            space = getDefaultDir();
        }
        File dir = FileUtil.mkdir(space);
        return FileUtil.file(dir, filename);
    }

    private String getDefaultDir() {
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isMac()) {
            return "";
        }
        if (osInfo.isWindows()) {
            return "C:\\Users\\admin\\Downloads";
        }
        return "/us";
    }
}
