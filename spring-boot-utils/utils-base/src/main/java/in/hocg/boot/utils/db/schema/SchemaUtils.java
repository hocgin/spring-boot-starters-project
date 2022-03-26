package in.hocg.boot.utils.db.schema;

import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.JdbcType;
import cn.hutool.db.meta.Table;
import com.google.common.collect.Lists;
import org.polyjdbc.core.PolyJDBC;
import org.polyjdbc.core.PolyJDBCBuilder;
import org.polyjdbc.core.dialect.Dialect;
import org.polyjdbc.core.dialect.DialectRegistry;
import org.polyjdbc.core.dialect.MsSqlDialect;
import org.polyjdbc.core.dialect.OracleDialect;
import org.polyjdbc.core.exception.SchemaInspectionException;
import org.polyjdbc.core.schema.model.AttributeBuilder;
import org.polyjdbc.core.schema.model.RelationBuilder;
import org.polyjdbc.core.schema.model.Schema;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hocgin on 2022/3/16
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class SchemaUtils {
    private Connection connection;
    private Dialect dialect = DialectRegistry.MYSQL.getDialect();


    public void createIfNeeded(Table table) {
        if (relationExists(table)) {
            return;
        }

        DataSource dataSource = DSFactory.get();
        PolyJDBC polyjdbc = PolyJDBCBuilder.polyJDBC(dialect).connectingToDataSource(dataSource).build();

        Schema schema = new Schema(dialect);

        String tableName = table.getTableName();
        RelationBuilder relationBuilder = schema.addRelation(tableName);

        relationBuilder.withAttribute().primaryKey("idx_pk").using(table.getPkNames().toArray(new String[0])).and();
        for (Column column : table.getColumns()) {
            handleRelation(relationBuilder, column);
        }
        relationBuilder.build();

        polyjdbc.schemaManager().create(schema);
    }

    private void handleRelation(RelationBuilder relationBuilder, Column column) {
        String columnName = column.getName();
        JdbcType typeEnum = column.getTypeEnum();
        boolean nullable = column.isNullable();
        int size = column.getSize();
        Object defaultValue = null;

        AttributeBuilder attributeBuilder;
        if (Lists.newArrayList(JdbcType.BIGINT).equals(typeEnum)) {
             attributeBuilder = relationBuilder.withAttribute().longAttr(columnName).withIntegerPrecision(size);
        } else if (Lists.newArrayList(JdbcType.VARBINARY).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().string(columnName).withMaxLength(size);
        } else if (Lists.newArrayList(JdbcType.DATE).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().date(columnName);
        } else if (Lists.newArrayList(JdbcType.TIMESTAMP).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().timestamp(columnName);
        } else if (Lists.newArrayList(JdbcType.BOOLEAN).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().booleanAttr(columnName);
        } else if (Lists.newArrayList(JdbcType.INTEGER).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().integer(columnName).withIntegerPrecision(size);
        } else if (Lists.newArrayList(JdbcType.CHAR).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().character(columnName);
        } else if (Lists.newArrayList(JdbcType.FLOAT).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().floatAttr(columnName);
        } else if (Lists.newArrayList(JdbcType.LONGNVARCHAR).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().text(columnName);
        } else if (Lists.newArrayList(JdbcType.NUMERIC).equals(typeEnum)) {
            attributeBuilder = relationBuilder.withAttribute().number(columnName).withDecimalPrecision(size);
        } else {
            throw new UnsupportedOperationException("不支持的类型");
        }
        if (!nullable) {
            attributeBuilder.notNull();
        }
        attributeBuilder.withDefaultValue(defaultValue);
    }

    public String getSchema() {
        try {
            return connection.getSchema();
        } catch (SQLException e) {
            return "";
        }
    }

    public boolean relationExists(Table table) {
        return relationExists(table.getTableName(), getSchema());
    }

    /**
     * 表是否存在
     *
     * @param name       表名
     * @param schemaName 库名
     * @return
     */
    public boolean relationExists(String name, String schemaName) {
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            String catalog = connection.getCatalog();
            ResultSet resultSet = metadata.getTables(catalog, this.convertCase(schemaName, metadata), this.convertCase(name, metadata), new String[]{"TABLE"});
            if (schemaName != null) {
                return resultSet.next();
            } else {
                String tableSchemaName;
                do {
                    if (!resultSet.next()) {
                        return false;
                    }
                    tableSchemaName = resultSet.getString("TABLE_SCHEM");
                } while (tableSchemaName != null && !tableSchemaName.equalsIgnoreCase("public") && !tableSchemaName.equals("") && (!(this.dialect instanceof MsSqlDialect) || !tableSchemaName.equalsIgnoreCase("dbo")) && (!(this.dialect instanceof OracleDialect) || !tableSchemaName.equalsIgnoreCase("system")));

                return true;
            }
        } catch (SQLException var8) {
            throw new SchemaInspectionException("RELATION_LOOKUP_ERROR", "Failed to obtain tables metadata when checking table " + name, var8);
        }
    }

    private String convertCase(String identifier, DatabaseMetaData metadata) throws SQLException {
        if (identifier != null && !identifier.isEmpty()) {
            if (metadata.storesLowerCaseIdentifiers()) {
                return identifier.toLowerCase();
            } else {
                return metadata.storesUpperCaseIdentifiers() ? identifier.toUpperCase() : identifier;
            }
        } else {
            return identifier;
        }
    }
}
