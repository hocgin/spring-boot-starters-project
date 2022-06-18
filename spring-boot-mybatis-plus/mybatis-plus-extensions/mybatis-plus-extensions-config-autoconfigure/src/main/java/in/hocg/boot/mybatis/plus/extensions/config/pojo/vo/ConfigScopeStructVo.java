package in.hocg.boot.mybatis.plus.extensions.config.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class ConfigScopeStructVo implements Serializable {
    /**
     * 域名称
     */
    private String title;
    /**
     * 域编号
     */
    private String scope;
    /**
     * 描述信息
     */
    private String remark;

    private List<ConfigScopeItemVo> items = Collections.emptyList();

}
