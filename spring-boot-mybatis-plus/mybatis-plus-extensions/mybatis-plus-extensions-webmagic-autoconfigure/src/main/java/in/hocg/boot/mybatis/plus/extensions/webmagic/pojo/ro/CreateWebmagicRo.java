package in.hocg.boot.mybatis.plus.extensions.webmagic.pojo.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/6/18
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Setter
@Getter
@Accessors(chain = true)
public class CreateWebmagicRo {
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("完成状态")
    private String status;
    @ApiModelProperty("失败原因")
    private String failReason;
    @ApiModelProperty("拉取的地址")
    private String pullUrl;
    @ApiModelProperty("拉取的数据")
    private String pullData;
    @ApiModelProperty("完成时间")
    private LocalDateTime finishedAt;
}
