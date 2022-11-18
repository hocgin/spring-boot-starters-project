package in.hocg.boot.excel.autoconfiguration.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
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
import in.hocg.boot.excel.autoconfiguration.annotation.ExcelUnique;
import in.hocg.boot.utils.LangUtils;
import in.hocg.boot.validation.core.ValidatorUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
@Slf4j
public class ValidReadListener<T> implements ReadListener<T> {
    @Getter
    private final List<Result<T>> validRow = new ArrayList<>();

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
        log.warn("读取 Excel 异常", exception);
    }

    @Override
    public void invoke(T row, AnalysisContext analysisContext) {
        // 这个每一条数据解析都会来调用
        Set<ConstraintViolation<T>> validate = ValidatorUtils.validate(row);
        validRow.add(new Result<>(row, new ArrayList<>(validate)));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成了 都会来调用
        if (CollUtil.isEmpty(validRow)) {
            return;
        }
        Class<?> rowClazz = validRow.get(0).getRow().getClass();
        List<Field> fields = Arrays.stream(ReflectUtil.getFields(rowClazz)).filter(field -> field.isAnnotationPresent(ExcelUnique.class)).collect(Collectors.toList());
        if (CollUtil.isEmpty(fields)) {
            return;
        }
        List<T> rows = validRow.stream().map(Result::getRow).collect(Collectors.toList());
        for (Field field : fields) {
            ExcelUnique unique = field.getAnnotation(ExcelUnique.class);
            String fieldName = field.getName();
            List<Object> dupValues = LangUtils.getDuplicateElements(rows.stream().map(t -> ReflectUtil.getFieldValue(t, field)).filter(Objects::nonNull).collect(Collectors.toList()));
            List<Result<T>> result = validRow.stream().filter(tResult -> dupValues.contains(ReflectUtil.getFieldValue(tResult.getRow(), field))).collect(Collectors.toList());
            for (Result<T> tResult : result) {
                tResult.addErrorMessage(fieldName, unique.message());
            }
        }
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

    public boolean hasError() {
        return validRow.stream().anyMatch(Result::hasError);
    }

    @Getter
    @Setter
    public static class Result<T> {
        private final T row;
        private final Map<String, List<String>> validateMaps;

        public Result(T row, List<ConstraintViolation<T>> validate) {
            this.row = row;
            this.validateMaps = validate.parallelStream().collect(Collectors.groupingBy(item -> item.getPropertyPath().toString(),
                Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())));
        }

        public boolean hasError() {
            return !validateMaps.isEmpty();
        }

        public boolean hasError(String fieldName) {
            return !validateMaps.getOrDefault(fieldName, Collections.emptyList()).isEmpty();
        }

        public List<String> getErrorMessage(String fieldName) {
            return validateMaps.get(fieldName);
        }

        public String getFormatErrorMessage(String fieldName) {
            return StrUtil.join(System.lineSeparator(), getErrorMessage(fieldName));
        }

        public void addErrorMessage(String fieldName, String message) {
            List<String> errorMessages = validateMaps.getOrDefault(fieldName, new ArrayList<>());
            errorMessages.add(message);
            validateMaps.put(fieldName, errorMessages);
        }
    }

    @RequiredArgsConstructor
    public static class ValidSheetWriteHandler<T> implements CellWriteHandler {
        private final List<Result<T>> validRows;
        private final int headRowNumber;

        @Override
        public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            int rowIndex = cell.getRowIndex();
            int index = rowIndex - headRowNumber;
            boolean isNotHead = BooleanUtils.isFalse(isHead) && Objects.nonNull(head);
            if (isNotHead && index < validRows.size()) {
                String fieldName = head.getFieldName();
                Result<T> validResult = validRows.get(index);
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
                column, row, column + 1, row));
            comment.setString(new XSSFRichTextString(message));
            return comment;
        }
    }
}
