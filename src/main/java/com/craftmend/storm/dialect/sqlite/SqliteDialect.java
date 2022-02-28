package com.craftmend.storm.dialect.sqlite;

import com.craftmend.storm.api.enums.KeyType;
import com.craftmend.storm.dialect.Dialect;
import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.objects.RelationField;
import com.craftmend.storm.utils.ColumnDefinition;
import com.craftmend.storm.utils.Reflection;

public class SqliteDialect implements Dialect {

    @Override
    public ColumnDefinition compileColumn(ParsedField<?> modelField) {
        String sqlTypeDeclaration = modelField.getAdapter().getSqlBaseType();
        sqlTypeDeclaration = sqlTypeDeclaration.replace("%max", modelField.getMax() + "");
        String column = modelField.getColumnName() + " " + sqlTypeDeclaration +

                (modelField.getKeyType() == KeyType.PRIMARY ? " PRIMARY KEY" : "") +

                (modelField.isAutoIncrement() ? " AUTOINCREMENT" : "") +

                (modelField.getDefaultValue() != null ? " DEFAULT(" +
                        (modelField.getAdapter().escapeAsString() ? "'" + modelField.getDefaultValue() + "'" : modelField.getDefaultValue())
                        + ")" : "") +

                (modelField.isNotNull() ? " NOT NULL" : "") +

                (modelField.isUnique() ? " UNIQUE" : "");
        String configuration = null;
        if (modelField.getKeyType() == KeyType.FOREIGN) {
            configuration = ", FOREIGN KEY (" + modelField.getColumnName() + ") REFERENCES " +
                    Reflection.getAnnotatedReference(modelField.getStorm(), modelField.getReflectedField())
                            .getTableName() + "(id)";
        }
        return new ColumnDefinition(column, configuration);
    }

}
