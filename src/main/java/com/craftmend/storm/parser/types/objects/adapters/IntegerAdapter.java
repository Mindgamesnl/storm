package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

public class IntegerAdapter extends StormTypeAdapter<Integer> {

    @Override
    public Integer fromString(Object sqlValue) {
        return Integer.valueOf(sqlValue.toString());
    }

    @Override
    public Object toSql(Integer value) {
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
