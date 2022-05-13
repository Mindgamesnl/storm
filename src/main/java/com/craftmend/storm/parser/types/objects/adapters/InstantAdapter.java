package com.craftmend.storm.parser.types.objects.adapters;


import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;

import java.sql.Timestamp;
import java.time.Instant;

public class InstantAdapter extends StormTypeAdapter<Instant> {

    @Override
    public Instant fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        return new Timestamp((Long) sqlValue).toInstant();
    }

    @Override
    public Object toSql(Storm storm, Instant value) {
        return Timestamp.from(value);
    }

    @Override
    public String getSqlBaseType() {
        return "DATETIME";
    }

    @Override
    public boolean escapeAsString() {
        return false;
    }

}
