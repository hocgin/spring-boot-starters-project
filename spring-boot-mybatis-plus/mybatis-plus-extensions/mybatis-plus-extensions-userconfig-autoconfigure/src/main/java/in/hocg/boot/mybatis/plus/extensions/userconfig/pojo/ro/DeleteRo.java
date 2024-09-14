package in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by hocgin on 2023/01/20
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@ApiModel
public class DeleteRo implements Serializable {
    @Size(min = 1, max = 100, message = "keys 数量超过限制")
    @NotNull(message = "keys 不能为空")
    @ApiModelProperty("key 列表")
    private List<Serializable> keys;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long optUserId;
    @ApiModelProperty(value = "来源", hidden = true)
    private String source;
}
