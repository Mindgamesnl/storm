package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class FloatAdapter extends StormTypeAdapter<Float> {

    @Override
    public Float fromSql(Object sqlValue) {
        if (sqlValue == null) return null;
        return Float.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Float value) {
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
