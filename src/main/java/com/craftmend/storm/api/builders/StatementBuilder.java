package com.craftmend.storm.api.builders;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.dialect.Dialect;
import com.craftmend.storm.parser.objects.ModelField;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatementBuilder {

    private StormModel model;

    public String buildSqlTableCreateStatement(Dialect dialect) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + model.parsed().getTableName() + " (");

        for (int i = 0; i < model.parsed().getParsedFields().length; i++) {
            ModelField mf = model.parsed().getParsedFields()[i];
            boolean isLast = model.parsed().getParsedFields().length == i + 1;
            sb.append(" " + dialect.compileColumn(mf) + (isLast ? "" : ","));
        }

        sb.append(")");
        return sb.toString();
    }

}
