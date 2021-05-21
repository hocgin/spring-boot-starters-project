package in.hocg.boot.message.autoconfigure.utils;

import cn.hutool.json.JSONUtil;
import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;
import in.hocg.boot.message.autoconfigure.data.PersistenceMessage;

/**
 * Created by hocgin on 2021/4/9
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MessageConvert {
    public static PersistenceMessage asPersistenceMessage(TransactionalMessage message) {
        PersistenceMessage persistenceMessage = new PersistenceMessage();
        persistenceMessage.setGroupSn(message.getGroupSn());
        persistenceMessage.setDestination(message.getDestination());
        persistenceMessage.setHeaders(JSONUtil.toJsonStr(message.getHeaders()));
        persistenceMessage.setPayload(message.getPayload());
        persistenceMessage.setPreparedAt(message.getPreparedAt());
        return persistenceMessage;
    }
}
