package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class StringAdapter extends StormTypeAdapter<String> {
    @Override
    public String fromSql(Object sqlValue) {
        if (sqlValue == null) return null;
        return sqlValue.toString();
    }

    @Override
    public Object toSql(String value) {
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
