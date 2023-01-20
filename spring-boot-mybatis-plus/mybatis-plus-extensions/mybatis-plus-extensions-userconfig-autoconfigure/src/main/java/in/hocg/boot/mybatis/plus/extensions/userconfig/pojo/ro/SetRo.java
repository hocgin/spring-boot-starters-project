package in.hocg.boot.mybatis.plus.extensions.userconfig.pojo.ro;

import in.hocg.boot.utils.struct.KeyValue;
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
public class SetRo implements Serializable {

    @Size(min = 1, max = 100, message = "列表数量超过限制")
    @NotNull(message = "列表不能为空")
    @ApiModelProperty("列表")
    private List<KeyValue> list;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long optUserId;
    @ApiModelProperty(value = "来源", hidden = true)
    private String source;
}
