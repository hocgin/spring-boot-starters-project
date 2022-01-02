package in.hocg.boot.excel.autoconfiguration.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;

import java.io.File;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ExcelUtilsTest {


    public static void main(String[] args) {
        ExcelWriter excelWriter = EasyExcel.write(new File(""), ExcelUtils.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("特殊号码").build();
        ExcelUtils.write(ExcelUtilsTest::paging, new PageRo(), data -> {
            excelWriter.write(data, writeSheet);
        });
        excelWriter.finish();
    }

    public static Page paging(PageRo ro) {
        return new Page();
    }
}
