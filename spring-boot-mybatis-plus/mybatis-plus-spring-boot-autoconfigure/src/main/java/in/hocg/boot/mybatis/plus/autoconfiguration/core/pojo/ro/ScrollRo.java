package in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2021/11/29
 * email: hocgin@gmail.com
 * 滚动翻页
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ScrollRo extends BasicRo {
    private Serializable nextId;
    private Integer size = 20;
}
