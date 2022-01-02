package in.hocg.boot.excel.sample.controller;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import in.hocg.boot.excel.autoconfiguration.utils.ExcelUtils;
import in.hocg.boot.excel.sample.IndexService;
import in.hocg.boot.excel.sample.vo.ExcelDataVo;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class WriteController {
    private final IndexService service;

    @ApiOperation("注解 - 导出")
    @GetMapping("/annotation")
    public void annotation(HttpServletResponse response) throws IOException {
        String fname = "文件_注解";
        PageRo ro = new PageRo();
        ro.setSize(100);

        ExcelUtils.filename(response, fname);

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), ExcelDataVo.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
        ExcelUtils.write(service::paging, ro, data -> excelWriter.write(data, writeSheet));
        excelWriter.finish();
    }

    @SneakyThrows
    @ApiOperation("填充 - 导出")
    @GetMapping("/fill")
    public void fill(HttpServletResponse response) {
        String fname = "文件_填充";
        PageRo ro = new PageRo();
        ro.setSize(100);

        ExcelUtils.filename(response, fname);

        File templateFile = ResourceUtils.getFile(StrUtil.format("{}{}", ClassUtil.getClassPath(), "template/export-fill.xlsx"));
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateFile.getAbsolutePath()).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        ExcelUtils.write(service::paging, ro, data -> excelWriter.fill(data, writeSheet));
        excelWriter.finish();
    }

    @SneakyThrows
    @ApiOperation("模版 - 导出")
    @GetMapping("/template")
    public void template(HttpServletResponse response) {
        String fname = "文件_模版";
        PageRo ro = new PageRo();
        ro.setSize(100);

        ExcelUtils.filename(response, fname);

        File templateFile = ResourceUtils.getFile(StrUtil.format("{}{}", ClassUtil.getClassPath(), "template/export-template.xlsx"));
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), ExcelDataVo.class).withTemplate(templateFile).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        ExcelUtils.write(service::paging, ro, data -> excelWriter.write(data, writeSheet));
        excelWriter.finish();
    }
}
