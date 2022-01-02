package in.hocg.boot.excel.sample.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;

import java.util.Map;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ValidReadListener<T> implements ReadListener<T> {
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        // 这个每一条数据解析都会来调用
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成了 都会来调用
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        // 额外信息（批注、超链接、合并单元格信息读取）
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        // 这里会一行行的返回头
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        // 是否读取下一行
        return ReadListener.super.hasNext(context);
    }
}
