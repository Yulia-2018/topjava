package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public class ConverterForTime implements Converter<String, LocalTime> {
    public LocalTime convert(String localTime) {
        return parseLocalTime(localTime);
    }
}
