package com.craftmend.storm.parser.types.objects;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;

public abstract class StormTypeAdapter<T> {

    public abstract T fromSql(ParsedField parsedField, Object sqlValue);
    public abstract Object toSql(Storm storm, T value);
    public abstract String getSqlBaseType();
    public abstract boolean escapeAsString();

}
