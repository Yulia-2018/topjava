package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExcess;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return getWithExcess(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllFiltered");
        LocalDate validStartDate = startDate.equals("") ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate validEndDate = endDate.equals("") ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime validStartTime = startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime validEndTime = endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime);
        final List<Meal> allFilteredByDate = service.getAllFilteredByDate(authUserId(), validStartDate, validEndDate);
        return getFilteredWithExcess(allFilteredByDate, authUserCaloriesPerDay(), validStartTime, validEndTime);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}