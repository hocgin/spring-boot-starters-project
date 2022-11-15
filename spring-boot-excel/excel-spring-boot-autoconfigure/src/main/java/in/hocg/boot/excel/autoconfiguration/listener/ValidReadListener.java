package in.hocg.boot.excel.autoconfiguration.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.BooleanUtils;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.validation.core.ValidatorUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2022/1/2
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class ValidReadListener<T> implements ReadListener<T> {
    @Getter
    private final List<Result<T>> validRow = new ArrayList<>();

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
    }

    @Override
    public void invoke(T row, AnalysisContext analysisContext) {
        // 这个每一条数据解析都会来调用
        Set<ConstraintViolation<T>> validate = ValidatorUtils.validate(row);
        validRow.add(new Result<>(row, validate.isEmpty() ? Collections.emptyList() : new ArrayList<>(validate)));
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

    public List<T> getRows() {
        return validRow.stream().map(Result::getRow).collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class Result<T> {
        private final T row;
        private final Map<String, List<ConstraintViolation<T>>> validateMaps;

        public Result(T row, List<ConstraintViolation<T>> validate) {
            this.row = row;
            this.validateMaps = LangUtils.toGroup(validate, violation -> violation.getPropertyPath().toString());
        }

        public boolean hasError() {
            return !validateMaps.isEmpty();
        }

        public boolean hasError(String fieldName) {
            return !validateMaps.getOrDefault(fieldName, Collections.emptyList()).isEmpty();
        }

        public List<String> getErrorMessage(String fieldName) {
            return LangUtils.toList(validateMaps.get(fieldName), ConstraintViolation::getMessage);
        }

        public String getFormatErrorMessage(String fieldName) {
            return StrUtil.join(System.lineSeparator(), getErrorMessage(fieldName));
        }
    }

    @RequiredArgsConstructor
    public static class ValidSheetWriteHandler<T> implements CellWriteHandler {
        private final List<Result<T>> validRows;

        @Override
        public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            int rowIndex = cell.getRowIndex();
            if (BooleanUtils.isFalse(isHead) && Objects.nonNull(head) && rowIndex < validRows.size()) {
                String fieldName = head.getFieldName();
                Result<T> validResult = validRows.get(rowIndex - 1);
                if (validResult.hasError(fieldName)) {
                    String errorMessage = validResult.getFormatErrorMessage(fieldName);
                    Sheet sheet = writeSheetHolder.getSheet();
                    cell.setCellComment(createComment(sheet, errorMessage, rowIndex, cell.getColumnIndex()));
                }
            }
        }

        public Comment createComment(Sheet sheet, String message, Integer row, Integer column) {
            Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();
            Comment comment = drawingPatriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
                column, row, column + 1, row + 1));
            comment.setString(new XSSFRichTextString(message));
            return comment;
        }

    }
}
