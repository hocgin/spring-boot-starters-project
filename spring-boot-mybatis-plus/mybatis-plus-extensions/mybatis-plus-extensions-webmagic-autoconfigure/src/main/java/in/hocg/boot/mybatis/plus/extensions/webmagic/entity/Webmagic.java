package in.hocg.boot.mybatis.plus.extensions.webmagic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.struct.basic.enhance.CommonEntity;
import in.hocg.boot.mybatis.plus.extensions.context.constants.MyBatisPlusExtensionsConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * [BOOT] 爬虫采集表
 * </p>
 *
 * @author hocgin
 * @since 2022-06-16
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(MyBatisPlusExtensionsConstants.TABLE_PREFIX + "webmagic")
public class Webmagic extends CommonEntity<Webmagic> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("类型")
    @TableField("type")
    private String type;
    @ApiModelProperty("完成状态")
    @TableField("status")
    private String status;
    @ApiModelProperty("失败原因")
    @TableField("fail_reason")
    private String failReason;
    @ApiModelProperty("拉取的地址")
    @TableField("pull_url")
    private String pullUrl;
    @ApiModelProperty("拉取的数据")
    @TableField("pull_data")
    private String pullData;
    @ApiModelProperty("完成时间")
    @TableField("finished_at")
    private LocalDateTime finishedAt;


}
