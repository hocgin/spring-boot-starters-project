package in.hocg.boot.youtube.autoconfiguration.utils.data;

import com.google.api.client.util.DateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * Created by hocgin on 2022/3/30
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class YouTubeChannel {
    @ApiModelProperty("频道名称")
    private String title;
    @ApiModelProperty("频道地址")
    private String url;
    @ApiModelProperty("频道id")
    private String channelId;
    @ApiModelProperty("图片")
    private String imageUrl;
    @ApiModelProperty("最近发布时间")
    private DateTime publishedAt;

    // === 状态
    @ApiModelProperty("连接状态")
    private Boolean isLinked;
    @ApiModelProperty("长上传状态")
    private String longUploadsStatus;
    @ApiModelProperty("隐私状态")
    private String privacyStatus;

    // === 统计
    @ApiModelProperty("视频数量")
    private BigInteger videoCount;
    @ApiModelProperty("订阅数量")
    private BigInteger subscriberCount;
    @ApiModelProperty("观看数量")
    private BigInteger viewCount;
    @ApiModelProperty("是否隐藏订阅")
    private Boolean hiddenSubscriberCount;

}
