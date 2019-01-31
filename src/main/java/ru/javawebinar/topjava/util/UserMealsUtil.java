package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        final List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredWithExceeded.forEach(System.out::print);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return getFilteredWithExceededThroughStream(mealList, startTime, endTime, caloriesPerDay);
        //return getFilteredWithExceededThroughCycle(mealList, startTime, endTime, caloriesPerDay);
    }

    private static List<UserMealWithExceed> getFilteredWithExceededThroughStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> daysWithSumCalories = mealList.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(x -> TimeUtil.isBetween(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> new UserMealWithExceed(x.getDateTime(), x.getDescription(), x.getCalories(), daysWithSumCalories.get(x.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceededThroughCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> daysWithSumCalories = new HashMap<>();
        for (UserMeal meal : mealList) {
            daysWithSumCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExceeds.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), daysWithSumCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExceeds;
    }
}
