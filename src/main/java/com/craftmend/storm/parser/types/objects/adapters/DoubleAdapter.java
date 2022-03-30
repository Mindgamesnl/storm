package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class DoubleAdapter extends StormTypeAdapter<Double> {

    @Override
    public Double fromSql(Object sqlValue) {
        if (sqlValue == null) return null;
        return Double.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Double value) {
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
