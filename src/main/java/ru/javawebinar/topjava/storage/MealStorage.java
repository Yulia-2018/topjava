package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface MealStorage {

    AtomicInteger counter = new AtomicInteger(6);

    int incrementAndGetCounter();

    Meal update(Meal meal);

    Meal create(Meal meal);

    Meal get(int id);

    void delete(int id);

    List<Meal> getAll();
}
