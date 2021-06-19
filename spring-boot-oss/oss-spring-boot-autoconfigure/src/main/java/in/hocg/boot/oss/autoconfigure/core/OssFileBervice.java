package in.hocg.boot.oss.autoconfigure.core;

import cn.hutool.crypto.digest.DigestUtil;
import in.hocg.boot.oss.autoconfigure.exception.UploadOssException;
import in.hocg.boot.oss.autoconfigure.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface OssFileBervice {

    default String upload(MultipartFile file) {
        try {
            File uploadFile = FileUtils.createTempFile(file.getOriginalFilename()).toFile();
            file.transferTo(uploadFile);
            return this.upload(uploadFile);
        } catch (IOException e) {
            throw new UploadOssException("文件上传失败");
        }
    }

    default String upload(File file) {
        return this.upload(file, getFileName(file));
    }

    String upload(File file, String filename);

    default String upload(File file, String filename, String space) {
        try {
            return this.upload(Files.newInputStream(file.toPath()), filename, space);
        } catch (IOException e) {
            throw new UploadOssException("文件上传失败");
        }
    }

    String upload(InputStream is, String filename, String space);

    default String getFileName(File file) {
        return DigestUtil.md5Hex(file) + "/" + file.getName();
    }
}
