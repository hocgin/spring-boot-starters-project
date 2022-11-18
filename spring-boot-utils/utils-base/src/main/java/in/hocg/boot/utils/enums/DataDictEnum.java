package in.hocg.boot.utils.enums;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by hocgin on 2020/6/13.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public interface DataDictEnum extends CodeEnum {
    String getName();

    default Map<String, Serializable> asMapping() {
        Map<String, Serializable> maps = Maps.newHashMap();
        Class<? extends DataDictEnum> aClass = this.getClass();
        for (DataDictEnum enumConstant : aClass.getEnumConstants()) {
            maps.put(enumConstant.getCodeStr(), enumConstant.getName());
        }
        return maps;
    }
}
