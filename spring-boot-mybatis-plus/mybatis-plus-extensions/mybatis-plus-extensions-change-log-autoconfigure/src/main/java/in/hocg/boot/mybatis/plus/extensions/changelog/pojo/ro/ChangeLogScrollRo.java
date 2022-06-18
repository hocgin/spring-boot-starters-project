package in.hocg.boot.mybatis.plus.extensions.changelog.pojo.ro;

import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.ScrollRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/3/28
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
@Accessors(chain = true)
public class ChangeLogScrollRo extends ScrollRo {
    @ApiModelProperty("关键词搜索")
    private String keyword;
    @ApiModelProperty("操作人")
    private Long optUserId;
    @ApiModelProperty("操作时间")
    private LocalDateTime startAt;
    @ApiModelProperty("操作时间")
    private LocalDateTime endAt;
    @ApiModelProperty("引用对象")
    private Long refId;
    @ApiModelProperty("引用类型")
    private String refType;

    @ApiModelProperty("是否倒序")
    private Boolean orderDesc = true;
}
