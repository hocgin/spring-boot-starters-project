package in.hocg.boot.message.core.transactional;

import cn.hutool.core.util.IdUtil;
import in.hocg.boot.message.core.MessageFactory;
import in.hocg.boot.web.SpringContext;

import java.util.List;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class TransactionalMessageFactory implements MessageFactory {
    public static final TransactionalMessageFactory ME = new TransactionalMessageFactory();

    private TransactionalMessageFactory() {
    }

    public void prepare(TransactionalMessage message) {
        TransactionalMessageContext.add(message);
    }

    public void publish() {
        final List<TransactionalMessage> messages = TransactionalMessageContext.getAndClear();
        if (messages.isEmpty()) {
            return;
        }

        final String messageGroupSn = getMessageGroupSn();
        final TransactionalMessageService service = SpringContext.getBean(TransactionalMessageService.class);
        for (TransactionalMessage message : messages) {
            message.setGroupSn(messageGroupSn);
            service.insertMessage(message);
        }
    }

    public void clear() {
        TransactionalMessageContext.clear();
    }

    private String getMessageGroupSn() {
        return IdUtil.fastSimpleUUID();
    }
}
