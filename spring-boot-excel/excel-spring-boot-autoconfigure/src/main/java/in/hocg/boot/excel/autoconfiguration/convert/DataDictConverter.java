package in.hocg.boot.excel.autoconfiguration.convert;

import com.google.common.collect.Maps;
import in.hocg.boot.utils.enums.DataDictEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据字典转换
 *
 * @author hocgin
 */
public class DataDictConverter extends AsMappingConverter {

    public DataDictConverter(Class<? extends DataDictEnum> enumClass) {
        super(asMapping(enumClass));
    }

    public static Map<String, Serializable> asMapping(Class<? extends DataDictEnum> enumClass) {
        Map<String, Serializable> maps = Maps.newHashMap();
        for (DataDictEnum enumConstant : enumClass.getEnumConstants()) {
            maps.put(enumConstant.getCodeStr(), enumConstant.getName());
        }
        return maps;
    }

}
