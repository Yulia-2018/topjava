package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements MealStorage {

    private final AtomicInteger counter = new AtomicInteger(6);

    private Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public int incrementAndGetCounter() {
        return counter.incrementAndGet();
    }

    @Override
    public Meal update(Meal meal) {
        final int id = meal.getId();
        if (storage.replace(id, meal) == null) {
            return null;
        }
        return meal;
    }

    @Override
    public Meal create(Meal meal) {
        final int id = meal.getId();
        storage.put(id, meal);
        return meal;
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
