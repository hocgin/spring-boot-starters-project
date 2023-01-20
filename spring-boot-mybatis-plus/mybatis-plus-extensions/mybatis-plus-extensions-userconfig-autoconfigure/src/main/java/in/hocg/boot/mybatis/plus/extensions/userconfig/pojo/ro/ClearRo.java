package in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by hocgin on 2023/01/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ApiModel
public class ClearRo {

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long optUserId;
    @ApiModelProperty(value = "来源", hidden = true)
    private String source;
}
