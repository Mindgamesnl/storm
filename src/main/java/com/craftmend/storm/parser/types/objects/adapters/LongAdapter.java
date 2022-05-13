package com.craftmend.storm.parser.types.objects.adapters;


import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class LongAdapter extends StormTypeAdapter<Long> {
    @Override
    public Long fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return Long.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, Long value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "BIGINT";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }
}
