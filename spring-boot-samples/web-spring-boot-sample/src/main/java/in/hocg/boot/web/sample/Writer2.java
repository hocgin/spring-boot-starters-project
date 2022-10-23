package in.hocg.boot.web.sample;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2021/7/25
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Lazy
@Component
public class Writer2 {

    public Writer2() {
        System.out.println("工作2" + " 被加载!!!");
    }

    public void write(String message) {
        System.out.println("writerId" + ": " + message);
    }

}
