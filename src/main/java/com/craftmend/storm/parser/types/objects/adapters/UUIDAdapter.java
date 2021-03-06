package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

import java.util.UUID;

public class UUIDAdapter extends StormTypeAdapter<UUID> {

    @Override
    public UUID fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return UUID.fromString(sqlValue.toString());
    }

    @Override
    public Object toSql(Storm storm, UUID value) {
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
