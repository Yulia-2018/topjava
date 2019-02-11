package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapMealStorage implements MealStorage {

    protected Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public Meal update(Meal meal) {
        final int id = meal.getId();
        return storage.replace(id, meal);
    }

    @Override
    public Meal create(Meal meal) {
        final int id = meal.getId();
        return storage.put(id, meal);
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
