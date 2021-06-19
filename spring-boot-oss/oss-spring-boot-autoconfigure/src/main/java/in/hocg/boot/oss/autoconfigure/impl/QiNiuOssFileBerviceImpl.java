package in.hocg.boot.oss.autoconfigure.impl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import in.hocg.boot.oss.autoconfigure.exception.UploadOssException;
import in.hocg.boot.oss.autoconfigure.core.OssFileBervice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.InputStream;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
@RequiredArgsConstructor
public class QiNiuOssFileBerviceImpl implements OssFileBervice, InitializingBean {
    private final String accessKey;
    private final String secretKey;
    private final String space;
    private final String domain;
    private Auth auth;

    @Override
    public String upload(File file, String filename) {
        return this.upload(file, filename, space);
    }

    @Override
    public String upload(InputStream is, String filename, String space) {
        UploadManager uploadManager = new UploadManager(new Configuration(Region.region0()));
        String uploadToken = getUploadToken(space);
        String key;
        try {
            Response response = uploadManager.put(is, filename, uploadToken, null, null);
            JSONObject result = JSONUtil.parseObj(response.bodyString());
            key = result.getStr("key");
        } catch (QiniuException e) {
            log.error("上传文件到七牛失败, 响应内容为:[{}]", e.response, e);
            throw new UploadOssException("文件上传失败");
        }
        return getFileUrl(key);
    }

    private String getFileUrl(String key) {
        return URLUtil.completeUrl(domain, key);
    }

    private String getUploadToken(String bucket) {
        return auth.uploadToken(bucket);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        auth = Auth.create(accessKey, secretKey);
    }
}
