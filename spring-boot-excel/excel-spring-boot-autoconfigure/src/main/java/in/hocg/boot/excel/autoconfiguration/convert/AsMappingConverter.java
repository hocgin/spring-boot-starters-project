package in.hocg.boot.excel.autoconfiguration.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import in.hocg.boot.utils.LangUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * 枚举值转换
 *
 * @author hocgin
 */
public abstract class AsMappingConverter implements Converter<Serializable> {
    private final Map<String, Serializable> toMapping;
    private final Map<Serializable, String> asMapping;

    public AsMappingConverter(Map<String, Serializable> mapping) {
        this(mapping, LangUtils.reverse(mapping));
    }

    public AsMappingConverter(Map<String, Serializable> toMapping, Map<Serializable, String> asMapping) {
        this.toMapping = toMapping;
        this.asMapping = asMapping;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Serializable convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty property, GlobalConfiguration configuration) throws Exception {
        return toMapping.get(cellData.getStringValue());
    }

    @Override
    public WriteCellData<Serializable> convertToExcelData(Serializable value, ExcelContentProperty property, GlobalConfiguration configuration) throws Exception {
        return new WriteCellData<>(asMapping.get(value));
    }
}
