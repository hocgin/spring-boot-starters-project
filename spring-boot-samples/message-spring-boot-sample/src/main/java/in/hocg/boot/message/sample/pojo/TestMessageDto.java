package in.hocg.boot.message.sample.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/4/21
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
public class TestMessageDto implements Serializable {
    private String body;
}
