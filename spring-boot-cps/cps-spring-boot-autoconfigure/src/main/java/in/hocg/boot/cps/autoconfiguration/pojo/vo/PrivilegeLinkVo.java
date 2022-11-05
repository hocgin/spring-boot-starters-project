package in.hocg.boot.cps.autoconfiguration.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PrivilegeLinkVo implements Serializable {
    /**
     * 推广链接
     */
    private String privilegeUrl;
}
