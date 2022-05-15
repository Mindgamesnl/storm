package com.craftmend.storm.gson;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Locale;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

@Getter
@AllArgsConstructor
public class TemporalFaker implements TemporalAccessor {

    private final long seconds;
    private final int nanos;

    @Override
    public boolean isSupported(TemporalField field) {
        return false;
    }

    @Override
    public long getLong(TemporalField field) {
        if (field == INSTANT_SECONDS) {
            return seconds;
        }
        return 0;
    }

    @Override
    public int get(TemporalField field) {
        if (field == NANO_OF_SECOND) {
            return nanos;
        }
        return 0;
    }
}
