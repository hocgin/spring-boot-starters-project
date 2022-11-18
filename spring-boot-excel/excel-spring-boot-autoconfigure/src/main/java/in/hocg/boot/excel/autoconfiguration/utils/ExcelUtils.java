package in.hocg.boot.excel.autoconfiguration.utils;

import cn.hutool.core.lang.Pair;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.excel.autoconfiguration.listener.ValidReadListener;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;
import in.hocg.boot.utils.LangUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hocgin on 2021/6/11
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@UtilityClass
public class ExcelUtils {
    /**
     * 自动翻页写入所有数据
     *
     * @param paging   分页查询
     * @param ro       请求参数
     * @param consumer 消费函数
     * @param <R>      _
     */
    public <R extends PageRo> void write(Function<R, IPage<?>> paging, R ro, Consumer<List<?>> consumer) {
        ro.setPage(1);
        ro.setIsCount(false);
        Integer size = ro.getSize();
        List<?> records;
        do {
            records = paging.apply(ro).getRecords();
            consumer.accept(records);
            ro.setPage(ro.getPage() + 1);
        } while (records.size() < size);
    }

    @SneakyThrows
    public static void filename(HttpServletResponse response, String fname) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(fname, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }

    /**
     * 读取 - 校验 - 标记 - 导出
     *
     * @param response    响应
     * @param inputStream 输入流
     * @param returnClazz excel 实体
     * @param fileName    响应文件名
     * @param <T>
     * @return 读取的数据行
     */
    @SneakyThrows
    public <T> List<T> validImport(HttpServletResponse response, InputStream inputStream, Class<T> returnClazz, String fileName) {
        // 读取 / 校验
        ValidReadListener<T> validReadListener = new ValidReadListener<>();
        EasyExcelFactory.read(inputStream, returnClazz, validReadListener).sheet().doRead();

        ExcelUtils.filename(response, fileName);

        // 异常 / 导出
        List<ValidReadListener.Result<T>> validRows = validReadListener.getValidRow();
        if (!validRows.isEmpty()) {
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), returnClazz)
                .registerWriteHandler(new ValidReadListener.ValidSheetWriteHandler(validRows, 1)).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
            excelWriter.write(LangUtils.toList(validRows, ValidReadListener.Result::getRow), writeSheet);
            excelWriter.finish();
            return Collections.emptyList();
        }
        return validReadListener.getRows();
    }


    public <T> Pair<Boolean, List<ValidReadListener.Result<T>>> validImport(InputStream inputStream, Integer headRowNumber, Class<T> headClazz) {
        // 读取 / 校验
        ValidReadListener<T> validReadListener = new ValidReadListener<>();
        EasyExcelFactory.read(inputStream, headClazz, validReadListener).headRowNumber(headRowNumber).sheet().doRead();
        return new Pair<>(validReadListener.hasError(), validReadListener.getValidRow());
    }

    public <T> void validOutputTo(OutputStream outputStream, File templateFile, Integer headRowNumber, Class<T> headClazz, List<ValidReadListener.Result<T>> validRows) {
        if (!validRows.isEmpty()) {
            ExcelWriter excelWriter = EasyExcel.write(outputStream, headClazz).withTemplate(templateFile.getAbsolutePath())
                .relativeHeadRowIndex(0).needHead(false)
                .registerWriteHandler(new ValidReadListener.ValidSheetWriteHandler(validRows, headRowNumber)).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("sheet").build();
            excelWriter.write(LangUtils.toList(validRows, ValidReadListener.Result::getRow), writeSheet);
            excelWriter.finish();
        }
    }

}
