package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.ArrayList;
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
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(SecurityUtil.authUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return (meal.getUserId() == userId) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        final Meal meal = get(userId, id);
        return meal != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        final Meal meal = repository.get(id);
        return (meal != null && meal.getUserId() == userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        final List<Meal> meals = new ArrayList<>(repository.values());
        final List<Meal> filteredMeals = getAllFiltered(meals, meal -> meal.getUserId() == userId);
        filteredMeals.sort(mealComparator);
        return filteredMeals;
    }

/*  public List<Meal> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllFiltered");
        final List<Meal> meals = getAll(userId);
        return getAllFiltered(meals, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate) & DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }*/

    private List<Meal> getAllFiltered(List<Meal> meals, Predicate<Meal> filter) {
        return meals
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}

