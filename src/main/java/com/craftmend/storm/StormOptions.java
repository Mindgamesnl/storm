package com.craftmend.storm;

import com.craftmend.storm.logger.DefaultLogger;
import com.craftmend.storm.logger.StormLogger;
import com.google.gson.TypeAdapter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StormOptions {

    /**
     * Logger to use
     */
    private StormLogger logger = new DefaultLogger();

    /**
     * Additional gson type adapters to register
     */
    private Map<Class<?>, Object> typeAdapters = new HashMap<>();

}
