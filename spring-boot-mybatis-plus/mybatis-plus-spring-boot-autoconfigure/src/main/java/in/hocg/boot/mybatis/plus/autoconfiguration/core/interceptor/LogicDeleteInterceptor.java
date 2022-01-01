package in.hocg.boot.mybatis.plus.autoconfiguration.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.ColumnConstants;
import in.hocg.boot.mybatis.plus.autoconfiguration.core.context.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by hocgin on 2022/1/1
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@Slf4j
public class LogicDeleteInterceptor extends JsqlParserSupport implements InnerInterceptor {

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();

        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE) {
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(parserMulti(mpBs.sql(), mpBs));
        }
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object mpBs) {
        List<UpdateSet> updateSets = update.getUpdateSets();
        String tableName = update.getTable().getName();

        // 如果是逻辑删除
        if (!isNeedLogicDelete(update)) {
            return;
        }

        Long userId = UserContextHolder.getUserId();
        if (hasColumnName(tableName, ColumnConstants.DELETER) && Objects.nonNull(userId)) {
            updateSets.add(new UpdateSet(new Column(ColumnConstants.DELETER), new LongValue(userId)));
        }

        // 移除更新人 & 更新时间
        if (hasColumnName(tableName, ColumnConstants.LAST_UPDATER) || hasColumnName(tableName, ColumnConstants.LAST_UPDATED_AT)) {
            boolean hasUpdateColumn = false;

            // 移除更新人 & 更新时间字段
            Iterator<UpdateSet> iterator = updateSets.iterator();
            while (iterator.hasNext()) {
                UpdateSet updateSet = iterator.next();
                List<Column> columns = updateSet.getColumns();
                List<Expression> expressions = updateSet.getExpressions();
                for (int i = columns.size() - 1; i >= 0; i--) {
                    Column column = columns.get(i);
                    String columnName = column.getColumnName();
                    if (ColumnConstants.LAST_UPDATED_AT.equalsIgnoreCase(columnName)
                        || ColumnConstants.LAST_UPDATER.equalsIgnoreCase(columnName)) {
                        hasUpdateColumn = true;
                        columns.remove(i);
                        expressions.remove(i);
                    }
                }
                if (columns.isEmpty()) {
                    iterator.remove();
                }
            }

            if (!hasUpdateColumn) {
                return;
            }

            // 移除更新人 & 更新时间参数
            PluginUtils.MPBoundSql mpBsThis = (PluginUtils.MPBoundSql) mpBs;
            List<ParameterMapping> params = mpBsThis.parameterMappings();
            Iterator<ParameterMapping> paramsIterator = params.iterator();
            while (paramsIterator.hasNext()) {
                ParameterMapping item = paramsIterator.next();
                String propertyName = StrUtil.toUnderlineCase(item.getProperty());
                if (StrUtil.equalsIgnoreCase(propertyName, ColumnConstants.LAST_UPDATER)
                    || StrUtil.equalsIgnoreCase(propertyName, ColumnConstants.LAST_UPDATED_AT)) {
                    paramsIterator.remove();
                }
            }
            mpBsThis.parameterMappings(params);
        }
    }

    private boolean hasColumnName(String tableName, String columnName) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        return tableInfo.getFieldList().stream().map(TableFieldInfo::getColumn).anyMatch(s -> StrUtil.equalsIgnoreCase(columnName, s));
    }

    private String getTableLogicField(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return StringPool.EMPTY;
        }

        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (tableInfo == null || !tableInfo.isWithLogicDelete() || tableInfo.getLogicDeleteFieldInfo() == null) {
            return StringPool.EMPTY;
        }
        return tableInfo.getLogicDeleteFieldInfo().getColumn();
    }

    private boolean isNeedLogicDelete(Update update) {
        String logicFieldColumn = getTableLogicField(update.getTable().getName());
        List<UpdateSet> updateSets = update.getUpdateSets();
        for (UpdateSet updateSet : updateSets) {
            List<Column> columns = updateSet.getColumns();
            List<Expression> expressions = updateSet.getExpressions();
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                if (logicFieldColumn.equalsIgnoreCase(column.getColumnName())
                    && !(expressions.get(i) instanceof NullValue)) {
                    return true;
                }
            }
        }
        return false;
    }

}
