package com.craftmend.storm.parser.types;

import com.craftmend.storm.parser.types.objects.StormTypeAdapter;
import com.craftmend.storm.parser.types.objects.adapters.BooleanAdapter;
import com.craftmend.storm.parser.types.objects.adapters.IntegerAdapter;
import com.craftmend.storm.parser.types.objects.adapters.StringAdapter;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistry {

    private static Map<Class, StormTypeAdapter<?>> adapters = new HashMap<>();

    static {
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Boolean.class, new BooleanAdapter());
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
