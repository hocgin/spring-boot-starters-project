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
public class Writer1 {
    private final Writer2 writer2;

    @Lazy
    public Writer1(Writer2 writer2) {
        this.writer2 = writer2;
        System.out.println("工作1" + " 被加载!!!");
    }

    public String write(String message) {
        return "工作1" + message;
    }

}
