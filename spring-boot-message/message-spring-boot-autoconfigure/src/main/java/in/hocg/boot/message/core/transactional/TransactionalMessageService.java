package in.hocg.boot.message.core.transactional;


/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface TransactionalMessageService {

    boolean insertMessage(TransactionalMessage message);


}
