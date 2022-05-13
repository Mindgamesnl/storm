package com.craftmend.storm.parser.types.objects.adapters;

import com.craftmend.storm.Storm;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;

public class GsonTypeAdapter<T> extends StormTypeAdapter<T> {

    @SneakyThrows
    @Override
    public T fromSql(ParsedField parsedField, Object sqlValue) {
        if (sqlValue == null) return null;
        byte[] bdata = (byte[]) sqlValue;
        String s = new String(bdata);
        return parsedField.getStorm().getGson().fromJson(s, TypeToken.get(parsedField.getType()).getType());
    }

    @Override
    public Object toSql(Storm storm, Object value) {
        return storm.getGson().toJson(value).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getSqlBaseType() {
        return "BLOB";
    }

    @Override
    public boolean escapeAsString() {
        return true;
    }
}
