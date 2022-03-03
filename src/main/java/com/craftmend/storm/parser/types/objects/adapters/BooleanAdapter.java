package com.craftmend.storm.parser.types.objects.adapters;


import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class BooleanAdapter extends StormTypeAdapter<Boolean> {
    @Override
    public Boolean fromSql(Object sqlValue) {
        if (sqlValue == null) return null;
        return Boolean.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Boolean value) {
        return value;
    }

    @Override
    public String getSqlBaseType() {
        return "BOOLEAN";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }
}
