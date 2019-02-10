package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListStorage extends AbstractStorage<Integer> {

    protected List<Meal> storage = new CopyOnWriteArrayList<>(new ArrayList<>());

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doUpdate(Integer index, Meal meal) {
        storage.set(index, meal);
    }

    @Override
    protected void doSave(Integer index, Meal meal) {
        storage.add(meal);
    }

    @Override
    protected Meal doGet(Integer index) {
        return storage.get(index);
    }

    @Override
    protected void doDelete(Integer index) {
        storage.remove(index.intValue());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected List<Meal> getListMeals() {
        return new CopyOnWriteArrayList<>(new ArrayList<>(storage));
    }

    @Override
    protected Integer getSearchKey(int id) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

}
