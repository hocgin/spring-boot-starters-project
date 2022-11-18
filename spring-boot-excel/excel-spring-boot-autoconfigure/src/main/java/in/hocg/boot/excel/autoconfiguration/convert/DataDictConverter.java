package in.hocg.boot.excel.autoconfiguration.convert;

import in.hocg.boot.utils.enums.DataDictEnum;

/**
 * 数据字典转换
 *
 * @author hocgin
 */
public class DataDictConverter extends AsMappingConverter {

    public DataDictConverter(DataDictEnum enumClass) {
        super(enumClass.asMapping());
    }

}
