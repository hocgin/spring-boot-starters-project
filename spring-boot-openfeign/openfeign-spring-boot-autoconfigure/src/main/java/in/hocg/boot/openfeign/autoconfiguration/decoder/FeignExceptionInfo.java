package in.hocg.boot.openfeign.autoconfiguration.decoder;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/10/7
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
public class FeignExceptionInfo implements Serializable {
    private String timestamp;
    private Integer status;
    private String error;
    private String exception;
    private String message;
    private String path;
}
