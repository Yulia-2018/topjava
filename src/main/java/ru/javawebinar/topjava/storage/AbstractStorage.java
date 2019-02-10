package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractStorage<SK> implements Storage {

    public static final org.slf4j.Logger log = getLogger(AbstractStorage.class);

    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();

    public void update(Meal meal) {
        log.debug("Update " + meal);
        final int id = meal.getId();
        final SK searchKey = getExistedSearchKey(id);
        doUpdate(searchKey, meal);
    }

    public void save(Meal meal) {
        log.debug("Save " + meal);
        final int id = meal.getId();
        final SK searchKey = getNotExistedSearchKey(id);
        doSave(searchKey, meal);
    }

    public Meal get(int id) {
        log.debug("Get " + id);
        final SK searchKey = getExistedSearchKey(id);
        return doGet(searchKey);
    }

    public void delete(int id) {
        log.debug("Delete " + id);
        final SK searchKey = getExistedSearchKey(id);
        doDelete(searchKey);
    }

    public List<Meal> getAll() {
        log.debug("getAllMeals");
        final List<Meal> mealsList = getListMeals();
        mealsList.sort(MEAL_COMPARATOR);
        return mealsList;
    }

    private SK getNotExistedSearchKey(int id) {
        final SK searchKey = getSearchKey(id);
        final boolean exist = isExist(searchKey);
        if (exist) {
            log.debug("Meal " + id + " already exist");
        }
        return searchKey;
    }

    private SK getExistedSearchKey(int id) {
        final SK searchKey = getSearchKey(id);
        final boolean exist = isExist(searchKey);
        if (!exist) {
            log.debug("Meal " + id + " not exist");
        }
        return searchKey;
    }

    protected abstract List<Meal> getListMeals();

    protected abstract void doUpdate(SK searchKey, Meal meal);

    protected abstract void doSave(SK searchKey, Meal meal);

    protected abstract Meal doGet(SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract SK getSearchKey(int id);

    protected abstract boolean isExist(SK searchKey);
}

