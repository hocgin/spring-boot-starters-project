package in.hocg.boot.message.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * [基础模块] 持久化消息表
 * </p>
 *
 * @author hocgin
 * @since 2020-07-20
 */
@Setter
@Getter
@Accessors(chain = true)
public class PersistenceMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 消息组编号
     */
    private String groupSn;
    /**
     * 消息标记
     */
    private String destination;
    /**
     * 消息体
     */
    private String payload;
    /**
     * 消息头
     */
    private String headers;
    /**
     * 消息状态[0=>为准备状态;1=>已发布]
     */
    private Integer published;
    private LocalDateTime preparedAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

}
