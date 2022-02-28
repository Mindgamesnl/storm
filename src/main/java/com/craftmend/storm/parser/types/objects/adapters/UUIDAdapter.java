package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

import java.util.UUID;

public class UUIDAdapter extends StormTypeAdapter<UUID> {

    @Override
    public UUID fromString(Object sqlValue) {
        return UUID.fromString(sqlValue.toString());
    }

    @Override
    public Object toSql(UUID value) {
        return value.toString();
    }

    @Override
    public String getSqlBaseType() {
        return "VARCHAR(36)";
    }

    @Override
    public boolean escapeAsString() {
        return true;
    }
}
