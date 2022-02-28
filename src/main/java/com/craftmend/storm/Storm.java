package com.craftmend.storm;

import com.craftmend.storm.connection.StormDriver;
import java.util.logging.Logger;

public class Storm {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());
    private StormDriver driver;

    public Storm(StormDriver driver) {
        this.driver = driver;
    }

}
