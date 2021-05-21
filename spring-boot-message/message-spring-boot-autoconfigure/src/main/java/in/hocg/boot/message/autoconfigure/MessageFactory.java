package in.hocg.boot.message.autoconfigure;

import in.hocg.boot.message.autoconfigure.service.local.LocalMessageService;
import in.hocg.boot.message.autoconfigure.service.normal.NormalMessageService;
import in.hocg.boot.web.autoconfiguration.SpringContext;

/**
 * Created by hocgin on 2020/7/20.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class MessageFactory {

    public static LocalMessageService local() {
        return SpringContext.getBean(LocalMessageService.class);
    }

    public static NormalMessageService normal() {
        return SpringContext.getBean(NormalMessageService.class);
    }
}
