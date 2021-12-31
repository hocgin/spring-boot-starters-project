package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2021/12/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteRo extends BasicRo {
    private List<Long> id = Collections.emptyList();
}
