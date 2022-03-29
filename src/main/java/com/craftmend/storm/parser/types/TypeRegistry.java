package com.craftmend.storm.parser.types;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.craftmend.storm.parser.types.objects.adapters.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TypeRegistry {

    private static Map<Class, StormTypeAdapter<?>> adapters = new HashMap<>();

    static {
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Boolean.class, new BooleanAdapter());
        adapters.put(UUID.class, new UUIDAdapter());
        adapters.put(Long.class, new LongAdapter());
        adapters.put(Double.class, new DoubleAdapter());
    }

    public static void registerAdapter(Class type, StormTypeAdapter<?> adapter) {
        adapters.put(type, adapter);
    }

    public static <T> StormTypeAdapter<?> getAdapterFor(Class<T> type) {
        StormTypeAdapter<?> a = adapters.get(type);
        if (a == null) throw new IllegalStateException("There's no registered adapter for " + type.getName());
        return a;
    }

}
