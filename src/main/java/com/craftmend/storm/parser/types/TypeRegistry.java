package com.craftmend.storm.parser.types;

import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.craftmend.storm.parser.types.objects.adapters.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypeRegistry {

    private static Map<Class, StormTypeAdapter<?>> adapters = new HashMap<>();
    private static StormTypeAdapter<Object> gsonAdapter = new GsonTypeAdapter();

    static {
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Boolean.class, new BooleanAdapter());
        adapters.put(UUID.class, new UUIDAdapter());
        adapters.put(Long.class, new LongAdapter());
        adapters.put(Double.class, new DoubleAdapter());
        adapters.put(Float.class, new FloatAdapter());
        adapters.put(Instant.class, new InstantAdapter());
    }

    public static void registerAdapter(Class type, StormTypeAdapter<?> adapter) {
        adapters.put(type, adapter);
    }

    public static <T> StormTypeAdapter<T> getAdapterFor(ParsedField<T> tParsedField) {
        if (tParsedField.isUseBlob()) {
            return new GsonTypeAdapter<T>();
        }
        StormTypeAdapter<?> a = adapters.get(tParsedField.getType());
        if (a == null) throw new IllegalStateException("There's no registered adapter for " + tParsedField.getType().getName());
        return (StormTypeAdapter<T>) a;
    }
}
