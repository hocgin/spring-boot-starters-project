package in.hocg.boot.web.datastruct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2020/2/25.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@ApiModel("键值对")
@Accessors(chain = true)
public class KeyValue implements Serializable {
    @ApiModelProperty(value = "键, 一般是用于描述文本", example = "启用")
    private String key;
    @ApiModelProperty(value = "值, 一般是用于提交的值", example = "1")
    private Object value;
}
