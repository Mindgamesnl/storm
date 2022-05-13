package com.craftmend.storm;

import com.craftmend.storm.logger.DefaultLogger;
import com.craftmend.storm.logger.StormLogger;
import lombok.Data;

@Data
public class StormOptions {

    /**
     * Logger to use
     */
    private StormLogger logger = new DefaultLogger();

}
