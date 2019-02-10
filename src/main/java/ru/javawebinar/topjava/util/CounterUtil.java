package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterUtil {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int incrementAndGet() {
        return counter.incrementAndGet();
    }
}
