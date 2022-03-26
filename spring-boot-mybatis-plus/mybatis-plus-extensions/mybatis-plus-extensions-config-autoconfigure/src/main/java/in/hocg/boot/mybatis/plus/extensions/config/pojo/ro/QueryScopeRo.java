package in.hocg.boot.mybatis.plus.extensions.config.pojo.ro;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by hocgin on 2022/3/26
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class QueryScopeRo implements Serializable {
    private String scope;
    private Long refId;
}