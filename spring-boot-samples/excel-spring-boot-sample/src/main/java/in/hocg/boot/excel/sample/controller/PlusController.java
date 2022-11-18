package in.hocg.boot.excel.sample.controller;

import in.hocg.boot.excel.autoconfiguration.utils.ExcelUtils;
import in.hocg.boot.excel.sample.vo.ExcelDataVo;
import in.hocg.boot.utils.struct.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/plus")
@RequiredArgsConstructor
public class PlusController {


    @ApiOperation("导入 - 校验 - 导出错误")
    @GetMapping("/import")
    public Result<Void> valid(MultipartFile file, HttpServletResponse response) throws IOException {
        List<ExcelDataVo> rows = ExcelUtils.validImport(response, file.getInputStream(), ExcelDataVo.class, "追加校验错误");
        System.out.println(rows);
        return Result.success();
    }

}
