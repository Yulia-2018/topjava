package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_USER_1_ID = START_SEQ + 2;
    public static final int MEAL_USER_2_ID = START_SEQ + 3;
    public static final int MEAL_USER_3_ID = START_SEQ + 4;
    public static final int MEAL_ADMIN_1_ID = START_SEQ + 5;
    public static final int MEAL_ADMIN_2_ID = START_SEQ + 6;

    public static final Meal MEAL_USER_1 = new Meal(MEAL_USER_1_ID, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_USER_2 = new Meal(MEAL_USER_2_ID, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_USER_3 = new Meal(MEAL_USER_3_ID, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_ADMIN_1 = new Meal(MEAL_ADMIN_1_ID, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal MEAL_ADMIN_2 = new Meal(MEAL_ADMIN_2_ID, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
