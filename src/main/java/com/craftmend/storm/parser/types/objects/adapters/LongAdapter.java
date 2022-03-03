package com.craftmend.storm.parser.types.objects.adapters;


import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class LongAdapter extends StormTypeAdapter<Long> {
    @Override
    public Long fromSql(Object sqlValue) {
        if (sqlValue == null) return null;
        return Long.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Long value) {
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
