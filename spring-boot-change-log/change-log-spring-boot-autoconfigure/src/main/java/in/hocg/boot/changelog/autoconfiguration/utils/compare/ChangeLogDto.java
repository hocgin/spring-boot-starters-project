package in.hocg.boot.changelog.autoconfiguration.utils.compare;

import in.hocg.boot.utils.enums.ICode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/4/10.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
public class ChangeLogDto {
    @ApiModelProperty("日志类型:[订单]")
    private String refType;
    @ApiModelProperty("业务ID:[订单ID]")
    private Long refId;
    @ApiModelProperty("变更类型:[新增/修改/删除]")
    private ChangeType changeType;
    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;
    @ApiModelProperty("创建人")
    private Long creator;
    @ApiModelProperty("字段变更记录")
    private List<FieldChangeDto> change = Collections.emptyList();

    @Getter
    @RequiredArgsConstructor
    public enum ChangeType implements ICode {
        Update("update", "更新"),
        Insert("insert", "新增"),
        Delete("delete", "删除");
        private final Serializable code;
        private final String name;
    }
}
