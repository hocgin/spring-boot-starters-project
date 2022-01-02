package in.hocg.boot.excel.autoconfiguration.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.pojo.ro.PageRo;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
        String fileName = URLEncoder.encode(fname, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }
}
