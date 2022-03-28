package in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto;

import in.hocg.boot.mybatis.plus.extensions.changelog.enums.ChangeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/4/10.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class ChangeLogDto implements Serializable {
    @ApiModelProperty("日志类型:[订单]")
    private String refType;
    @ApiModelProperty("业务ID:[订单ID]")
    private Long refId;
    @ApiModelProperty("变更类型:[新增/修改/删除]")
    private ChangeType changeType;
    @ApiModelProperty("字段变更记录")
    private List<FieldChangeDto> change = Collections.emptyList();

}
