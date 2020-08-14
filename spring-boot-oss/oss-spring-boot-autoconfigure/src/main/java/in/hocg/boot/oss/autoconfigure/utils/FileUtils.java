package in.hocg.boot.oss.autoconfigure.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class FileUtils {
    public Path createTempFile(@NonNull String filename) throws IOException {
        return Files.createTempDirectory("temp_file")
            .resolve(filename);
    }
}
