package in.hocg.boot.message.autoconfigure.core;


import in.hocg.boot.message.autoconfigure.core.message.TransactionalMessage;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TransactionalMessageBervice {

    /**
     * 保存消息
     *
     * @param message 消息
     * @return 是否保存成功
     */
    boolean insertMessage(TransactionalMessage message);


}
