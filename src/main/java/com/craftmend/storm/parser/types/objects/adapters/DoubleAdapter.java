package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class DoubleAdapter extends StormTypeAdapter<Double> {

    @Override
    public Double fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return Double.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, Double value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "DOUBLE";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }
}
