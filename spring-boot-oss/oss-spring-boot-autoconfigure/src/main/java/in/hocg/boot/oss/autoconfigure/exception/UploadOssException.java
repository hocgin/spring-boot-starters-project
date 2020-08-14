package in.hocg.boot.oss.autoconfigure.exception;

/**
 * Created by hocgin on 2020/8/14
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class UploadOssException extends RuntimeException {
    public UploadOssException(String message) {
        super(message);
    }
}
