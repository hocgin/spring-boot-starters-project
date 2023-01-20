package in.hocg.boot.utils.struct;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2020/6/14.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Data
@Accessors(chain = true)
public class DictData {
    /**
     * 字典名称
     */
    private String title;
    /**
     * 字典标识
     */
    private String code;
    /**
     * 启用状态
     */
    private Boolean enabled;
    /**
     * 子项
     */
    private List<Item> items = Collections.emptyList();

    @Data
    @Accessors(chain = true)
    public static class Item {
        /**
         * 字典项名称
         */
        private String title;
        /**
         * 字典标识
         */
        private Serializable code;
        /**
         * 启用状态
         */
        private Boolean enabled;
    }
}
