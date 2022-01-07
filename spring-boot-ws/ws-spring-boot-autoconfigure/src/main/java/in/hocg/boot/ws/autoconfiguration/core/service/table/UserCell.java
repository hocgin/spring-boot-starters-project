package in.hocg.boot.ws.autoconfiguration.core.service.table;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class UserCell implements Serializable {
    /**
     * 会话唯一标识
     */
    private String sessionId;
    /**
     * 用户标识
     */
    private String userKey;
    /**
     * 会话创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 会话最后一次活跃时间
     */
    private LocalDateTime lastUpdatedAt;
    /**
     * 位置
     */
    private String url;
}
