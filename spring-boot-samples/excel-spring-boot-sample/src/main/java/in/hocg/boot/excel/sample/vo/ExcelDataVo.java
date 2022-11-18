package in.hocg.boot.excel.sample.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Getter
@Setter
public class ExcelDataVo {
    @Size(min = 2, message = "标题长度不符合")
    @NotNull(message = "标题不能为 NULL")
    @NotBlank(message = "标题不能为空")
    @ExcelProperty("标题")
    private String title;
    @NotNull(message = "年龄不能为 NULL")
    @ExcelProperty("年龄")
    private Integer age;
}
