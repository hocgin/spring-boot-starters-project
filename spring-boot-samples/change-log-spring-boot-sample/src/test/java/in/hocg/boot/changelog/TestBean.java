package in.hocg.boot.changelog;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by hocgin on 2021/6/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class TestBean {
    private String name;
    private String code;
    private String ip;
    private Long id;
}
