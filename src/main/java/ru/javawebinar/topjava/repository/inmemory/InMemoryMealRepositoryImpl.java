package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private static final Comparator<Meal> mealComparator = Comparator.comparing(Meal::getDateTime).reversed();
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS_1.forEach(meal -> save(1, meal));
        MealsUtil.MEALS_2.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        Integer id = meal.getId();
        if (meal.isNew()) {
            id = counter.incrementAndGet();
            meal.setId(id);
        } else if (get(userId, id) == null) {
            return null;
        }
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        repository.get(userId).put(id, meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        final Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        final Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilteredByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllFilteredByDate");
        return getAllFiltered(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllFiltered(int userId, Predicate<Meal> filter) {
        final Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? Collections.emptyList() : userMeals.values()
                .stream()
                .filter(filter)
                .sorted(mealComparator)
                .collect(Collectors.toList());
    }
}
