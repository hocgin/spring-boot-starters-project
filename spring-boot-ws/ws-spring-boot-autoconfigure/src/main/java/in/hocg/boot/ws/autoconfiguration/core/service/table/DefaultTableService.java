package in.hocg.boot.ws.autoconfiguration.core.service.table;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2022/1/4
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class DefaultTableService implements TableService {
    private final Map<String, UserCell> tables = Maps.newHashMap();

    @Override
    public void create(String sessionId) {
        tables.put(sessionId, new UserCell().setSessionId(sessionId)
            .setCreatedAt(LocalDateTime.now())
            .setLastUpdatedAt(LocalDateTime.now()));
    }

    @Override
    public void login(String sessionId, String userKey) {
        UserCell cell = tables.get(sessionId);
        if (Objects.isNull(cell)) {
            cell = new UserCell().setUserKey(userKey)
                .setSessionId(sessionId)
                .setCreatedAt(LocalDateTime.now())
                .setLastUpdatedAt(LocalDateTime.now());
        }
        tables.put(sessionId, cell);
    }

    @Override
    public void logout(String userKey) {
        tables.entrySet().removeIf(entry -> StrUtil.equals(entry.getValue().getUserKey(), userKey));
    }

    @Override
    public void remove(String sessionId) {
        tables.remove(sessionId);
    }

    @Override
    public void heartbeat(String sessionId) {
        UserCell cell = tables.get(sessionId);
        if (Objects.nonNull(cell)) {
            cell.setLastUpdatedAt(LocalDateTime.now());
        }
    }
}
