package in.hocg.boot.changelog.autoconfiguration.utils.compare;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2020/4/10.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class FieldChangeDto {
    @ApiModelProperty("字段名")
    private String fieldName;
    @ApiModelProperty("字段描述")
    private String fieldRemark;
    @ApiModelProperty("变更描述")
    private String changeRemark;
    @ApiModelProperty("值:变更前")
    private String beforeValue;
    @ApiModelProperty("值:变更后")
    private String afterValue;
}
