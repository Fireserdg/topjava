package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class MealsUtil {

    public static final int DEFAULT_CALORIES = 2000;

    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals,Predicate<Meal> filter, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealTo> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExcess(meals, meal -> TimeUtil.isBetween(meal.getTime(),
                LocalTime.MIN, LocalTime.MAX), caloriesPerDay);
    }

    public static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}