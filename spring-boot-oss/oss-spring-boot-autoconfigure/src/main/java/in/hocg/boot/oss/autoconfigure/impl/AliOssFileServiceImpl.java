package in.hocg.boot.oss.autoconfigure.impl;

import cn.hutool.core.util.URLUtil;
import com.aliyun.oss.OSSClient;
import in.hocg.boot.oss.autoconfigure.core.OssFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.InputStream;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AliOssFileServiceImpl implements OssFileService, InitializingBean {
    private final String accessKey;
    private final String secretKey;
    private final String space;
    private final String domain;
    private OSSClient ossClient;

    @Override
    public String upload(File file, String filename) {
        return this.upload(file, filename, space);
    }

    @Override
    public String upload(InputStream is, String filename, String space) {
        ossClient.putObject(space, filename, is);
        return getFileUrl(filename);
    }

    private String getFileUrl(String key) {
        String[] subStr = domain.split("://", 2);
        String prefix = subStr[0];
        String suffix = subStr[1];
        return URLUtil.completeUrl(String.format("%s://%s.%s", prefix, space, suffix), key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ossClient = new OSSClient(domain, accessKey, secretKey);
    }

}
