package in.hocg.boot.excel.sample.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
public class ExcelDataVo {
    @ExcelProperty("标题")
    private String title;
}
