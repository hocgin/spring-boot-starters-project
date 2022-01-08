package in.hocg.boot.excel.sample.controller;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import in.hocg.boot.excel.sample.IndexService;
import in.hocg.boot.excel.sample.vo.ExcelDataVo;
import in.hocg.boot.utils.struct.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class ReadController {
    private final IndexService service;

    @ApiOperation("校验 - 导入")
    @GetMapping("/valid")
    public Result<Void> valid() throws IOException {
        File file = ResourceUtils.getFile(StrUtil.format("{}{}", ClassUtil.getClassPath(), "import/file.xlsx"));
        EasyExcel.read(file, ExcelDataVo.class, new PageReadListener<>(service::handle))
            .sheet().doRead();
        return Result.success();
    }

    @ApiOperation("翻页读取 - 导入")
    @GetMapping("/batch")
    public Result<Void> batch() throws IOException {
        File file = ResourceUtils.getFile(StrUtil.format("{}{}", ClassUtil.getClassPath(), "import/file.xlsx"));
        EasyExcel.read(file, ExcelDataVo.class, new PageReadListener<>(service::handle))
            .sheet().doRead();
        return Result.success();
    }

    @ApiOperation("全部读取 - 导入")
    @GetMapping("/sync")
    public Result<Integer> sync() throws IOException {
        File file = ResourceUtils.getFile(StrUtil.format("{}{}", ClassUtil.getClassPath(), "import/file.xlsx"));
        List<ExcelDataVo> data = EasyExcel.read(file).head(ExcelDataVo.class).sheet().doReadSync();
        return Result.success(data.size());
    }

    @ApiOperation("web读取 - 导入")
    @GetMapping("/web")
    public Result<Void> web(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ExcelDataVo.class, new PageReadListener<>(service::handle))
            .sheet().doRead();
        return Result.success();
    }

}
