package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

public class ConverterForDate implements Converter<String, LocalDate> {
    public LocalDate convert(String localDate) {
        return parseLocalDate(localDate);
    }
}
