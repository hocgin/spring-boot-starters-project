package in.hocg.boot.message.core;


import in.hocg.boot.message.core.message.TransactionalMessage;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TransactionalMessageService {

    /**
     * 保存消息
     *
     * @param message 消息
     * @return 是否保存成功
     */
    boolean insertMessage(TransactionalMessage message);


}
