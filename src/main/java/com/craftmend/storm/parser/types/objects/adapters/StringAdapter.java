package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class StringAdapter extends StormTypeAdapter<String> {
    @Override
    public String fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return sqlValue.toString();
    }

    @Override
    public Object toSql(Storm storm, String value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "VARCHAR(%max)";
    }

    @Override
    public boolean escapeAsString() {
        return true;
    }
}
