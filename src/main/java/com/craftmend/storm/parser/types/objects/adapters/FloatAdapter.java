package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class FloatAdapter extends StormTypeAdapter<Float> {

    @Override
    public Float fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return Float.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, Float value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "FLOAT";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }
}
