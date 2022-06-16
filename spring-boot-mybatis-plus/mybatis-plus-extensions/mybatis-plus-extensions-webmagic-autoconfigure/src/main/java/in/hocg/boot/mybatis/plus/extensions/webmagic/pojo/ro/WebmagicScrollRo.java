package in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.ScrollRo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/6/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Setter
@Getter
@Accessors(chain = true)
public class WebmagicScrollRo extends ScrollRo {
    private String type;
    private String status;
    private Boolean asc = true;
}
