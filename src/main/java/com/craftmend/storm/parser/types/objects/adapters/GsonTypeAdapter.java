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
        String json;

        if (sqlValue instanceof Blob) {
            Blob blob = (Blob) sqlValue;
            json = new String(blob.getBytes(1, (int) blob.length()), StandardCharsets.UTF_8);
        } else if (sqlValue instanceof byte[]) {
            json = new String((byte[]) sqlValue, StandardCharsets.UTF_8);
        } else if (sqlValue instanceof String) {
            json = (String) sqlValue;
        } else {
            throw new IllegalArgumentException("Cannot convert " + sqlValue.getClass().getName() + " to Gson");
        }

        return parsedField.getStorm().getGson().fromJson(json, TypeToken.get(parsedField.getType()).getType());
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
