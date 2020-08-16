package in.hocg.boot.message.data.client.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@RequiredArgsConstructor
public enum PersistenceMessagePublished {
    Prepare(0, "准备状态"),
    Complete(1, "已完成状态");
    private final Integer code;
    private final String name;

}
