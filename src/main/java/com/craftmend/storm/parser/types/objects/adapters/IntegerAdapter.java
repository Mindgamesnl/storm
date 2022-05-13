package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class IntegerAdapter extends StormTypeAdapter<Integer> {

    @Override
    public Integer fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return Integer.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, Integer value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "INTEGER";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }


}
