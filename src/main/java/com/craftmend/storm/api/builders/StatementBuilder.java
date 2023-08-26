package com.craftmend.storm.api.builders;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.BaseStormModel;
import com.craftmend.storm.dialect.Dialect;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.utils.ColumnDefinition;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class StatementBuilder {

    private BaseStormModel model;

    public String buildSqlTableCreateStatement(Dialect dialect, Storm orm) {
        StringBuilder sb = new StringBuilder();
        List<String> footerConfigs = new ArrayList<>();
        sb.append("CREATE TABLE " + model.parsed(orm).getTableName() + " (");

        for (int i = 0; i < model.parsed(orm).getParsedFields().length; i++) {
            ParsedField mf = model.parsed(orm).getParsedFields()[i];
            boolean isLast = model.parsed(orm).getParsedFields().length == i + 1;
            ColumnDefinition cd = dialect.compileColumn(mf);
            sb.append(" " + cd.getColumnSql() + (isLast ? "" : ","));
            if (cd.getConfigurationSql() != null) {
                footerConfigs.add(cd.getConfigurationSql());
            }
        }

        int i = 0;
        if (!footerConfigs.isEmpty()) {
            for (String footerConfig : footerConfigs) {
                sb.append(footerConfig);
                if (i == footerConfig.length()) {
                    sb.append(", ");
                }
                i++;
            }
        }

        sb.append(")");
        return sb.toString();
    }

}
