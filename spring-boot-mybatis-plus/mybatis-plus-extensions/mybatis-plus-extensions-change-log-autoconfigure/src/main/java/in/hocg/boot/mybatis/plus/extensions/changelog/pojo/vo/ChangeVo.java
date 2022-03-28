package in.hocg.boot.mybatis.plus.extensions.changelog.pojo.vo;

import in.hocg.boot.mybatis.plus.extensions.changelog.pojo.dto.ChangeLogDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ChangeVo extends ChangeLogDto {
    @ApiModelProperty("日志ID")
    private Long id;
    @ApiModelProperty("日志编号")
    private String logSn;
}
