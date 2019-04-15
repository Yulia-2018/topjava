package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class MealTo extends BaseTo {

    @NotNull
    private LocalDateTime dateTime;

    @NotBlank
    @Size(min = 2, max = 120)
    private String description;

    @NotNull
    @Range(min = 10, max = 5000)
    private Integer calories;

    private boolean excess;

    public MealTo() {
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, Integer calories, boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MealTo mealTo = (MealTo) o;

        if (excess != mealTo.excess) return false;
        if (!dateTime.equals(mealTo.dateTime)) return false;
        if (!description.equals(mealTo.description)) return false;
        return calories.equals(mealTo.calories);
    }

    @Override
    public int hashCode() {
        int result = dateTime.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + calories.hashCode();
        result = 31 * result + (excess ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}