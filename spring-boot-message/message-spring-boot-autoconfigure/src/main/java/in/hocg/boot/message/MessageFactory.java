package in.hocg.boot.message;

import in.hocg.boot.message.service.local.LocalMessageService;
import in.hocg.boot.message.service.normal.NormalMessageService;
import in.hocg.boot.web.SpringContext;

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
