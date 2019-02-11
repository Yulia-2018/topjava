package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.MealsTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.CounterUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static Storage storage;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new ListStorage();
        storage.save(MealsTestData.MEAL_1);
        storage.save(MealsTestData.MEAL_2);
        storage.save(MealsTestData.MEAL_3);
        storage.save(MealsTestData.MEAL_4);
        storage.save(MealsTestData.MEAL_5);
        storage.save(MealsTestData.MEAL_6);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String dateTime = request.getParameter("dateTime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        Meal meal;
        if (Integer.parseInt(id) == 0) {
            meal = new Meal(CounterUtil.incrementAndGet(), LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
            storage.save(meal);
        } else {
            meal = storage.get(Integer.parseInt(id));
            meal.setDateTime(LocalDateTime.parse(dateTime));
            meal.setDescription(description);
            meal.setCalories(Integer.parseInt(calories));
            storage.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward, meals");

        String id = request.getParameter("id");
        String action = request.getParameter("action");
        if (action == null) {
            final List<MealTo> mealsWithExcess = MealsUtil.getFilteredWithExcess(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealsWithExcess);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                if (id == null) {
                    meal = new Meal();
                } else {
                    meal = storage.get(Integer.parseInt(id));
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("editmeal.jsp").forward(request, response);
    }
}
