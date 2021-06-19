package in.hocg.boot.message.autoconfigure;

import in.hocg.boot.message.autoconfigure.service.local.LocalMessageBervice;
import in.hocg.boot.message.autoconfigure.service.normal.NormalMessageBervice;
import in.hocg.boot.web.autoconfiguration.SpringContext;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MessageFactory {

    public static LocalMessageBervice local() {
        return SpringContext.getBean(LocalMessageBervice.class);
    }

    public static NormalMessageBervice normal() {
        return SpringContext.getBean(NormalMessageBervice.class);
    }
}
