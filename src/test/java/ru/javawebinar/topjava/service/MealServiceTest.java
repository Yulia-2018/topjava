package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_USER_2_ID, USER_ID);
        assertMatch(meal, MEAL_USER_2);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL_USER_3_ID, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_2_ID, USER_ID);
        final List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL_USER_6, MEAL_USER_5, MEAL_USER_4, MEAL_USER_3, MEAL_USER_1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(MEAL_USER_3_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDates1() {
        final List<Meal> meals = service.getBetweenDates(LocalDate.of(2018, Month.MAY, 5),
                LocalDate.of(2019, Month.MARCH, 1), USER_ID);
        assertMatch(meals, Collections.emptyList());
    }

    @Test
    public void getBetweenDates2() {
        final List<Meal> meals = service.getBetweenDates(LocalDate.of(2015, Month.MAY, 31),
                LocalDate.of(2018, Month.APRIL, 15), USER_ID);
        assertMatch(meals, MEAL_USER_6, MEAL_USER_5, MEAL_USER_4);
    }

    @Test
    public void getBetweenDateTimes() {
        final List<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 12, 5),
                LocalDateTime.of(2015, Month.MAY, 31, 19, 58), USER_ID);
        assertMatch(meals, MEAL_USER_5, MEAL_USER_4, MEAL_USER_3, MEAL_USER_2);
    }

    @Test
    public void getAll() {
        final List<Meal> meals = service.getAll(ADMIN_ID);
        assertMatch(meals, MEAL_ADMIN_2, MEAL_ADMIN_1);
    }

    @Test
    public void update() {
        final Meal meal = new Meal(MEAL_USER_3);
        meal.setCalories(480);
        service.update(meal, USER_ID);
        assertMatch(service.get(MEAL_USER_3_ID, USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        service.update(MEAL_USER_3, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 16, 15), "Админ полдник", 380);
        service.create(newMeal, ADMIN_ID);
        assertMatch(service.getAll(ADMIN_ID), MEAL_ADMIN_2, newMeal, MEAL_ADMIN_1);
    }
}