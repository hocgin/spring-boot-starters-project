package in.hocg.boot.message.data.client.jdbc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created by hocgin on 2020/8/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class PersistenceMessage {
    private Long id;
    private String groupSn;
    private String destination;
    private String payload;
    private String headers;
    private Integer published;
    private LocalDateTime preparedAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
