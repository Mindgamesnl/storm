package com.craftmend.storm.logger;

import java.util.logging.Logger;

public class DefaultLogger implements StormLogger {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    public DefaultLogger() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
    }

    @Override
    public void warning(String string) {
        logger.warning(string);
    }

    @Override
    public void info(String string) {
        logger.info(string);
    }
}
