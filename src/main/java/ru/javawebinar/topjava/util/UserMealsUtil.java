package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<Meal> mealList = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<MealTo> exceeded = getFilteredWithExceeded(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<MealTo> exceededStream = getFilteredWithExceededWithStream(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<MealTo> inOneReturn = getFilteredWithExceededInOneReturn(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<MealTo> withCycleOneReturn = getFilteredWithExceededWithCycleOneReturn(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        exceeded.forEach(System.out::println);
        System.out.println();
        exceededStream.forEach(System.out::println);
        System.out.println();
        inOneReturn.forEach(System.out::println);
        System.out.println();
        withCycleOneReturn.forEach(System.out::println);

}

    /**
     * Use forEach
     *
     * @param mealList       mealList
     * @param startTime      startTime
     * @param endTime        endTime
     * @param caloriesPerDay caloriesPerDay
     * @return list of MealTo
     */
    public static List<MealTo> getFilteredWithExceeded(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> map = new HashMap<>();
        mealList.forEach((meal) ->  map.merge(
                meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        List<MealTo> mealWithExceed = new ArrayList<>();
        mealList.forEach((meal) -> {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceed.add(createWithExcess(meal, map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return mealWithExceed;
    }

    /**
     * Use Stream API Java 8
     *
     * @param mealList       mealList
     * @param startTime      startTime
     * @param endTime        endTime
     * @param caloriesPerDay caloriesPerDay
     * @return list of MealTo
     */
    public static List<MealTo> getFilteredWithExceededWithStream(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = mealList.stream().
                collect(Collectors.toMap(k -> k.getDateTime().toLocalDate(), Meal::getCalories, Integer::sum));

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(v -> new MealTo(v.getDateTime(), v.getDescription(), v.getCalories(),
                        map.get(v.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }


    public static List<MealTo> getFilteredWithExceededWithCycleOneReturn(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDay = new HashMap<>();
        Map<LocalDate, AtomicBoolean> exceededMap = new HashMap<>();

        List<MealTo> mealWithExceeded = new ArrayList<>();
        mealList.forEach(meal -> {
            AtomicBoolean wrapBoolean = exceededMap
                    .computeIfAbsent(meal.getDateTime().toLocalDate(), date -> new AtomicBoolean());
            Integer dailyCalories = caloriesSumByDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            if (dailyCalories > caloriesPerDay) {
                wrapBoolean.set(true);
            }
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExceeded.add(createWithExcess(meal, wrapBoolean));
            }

        });

        return mealWithExceeded;
    }

    /**
     * Use Stream API Java 8 In one return.
     *
     * @param mealList       mealList
     * @param startTime      startTime
     * @param endTime        endTime
     * @param caloriesPerDay caloriesPerDay
     * @return list of MealTo
     */
    public static List<MealTo> getFilteredWithExceededInOneReturn(List<Meal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream().collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate()))
                .values().stream().flatMap(mealPerDay -> {
                    boolean exceed = mealPerDay.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
            return mealPerDay.stream()
                    .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .map(meal -> createWithExcess(meal, exceed));
        }).collect(Collectors.toList());
    }

    /**
     * Create meal with excess.
     *
     * @param meal meal
     * @param excess excess
     * @return MealTo
     */
    public static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    /**
     * Create meal with excess Atomic Boolean.
     *
     * @param meal meal
     * @param excess excess
     * @return MealTo
     */
    public static MealTo createWithExcess(Meal meal, AtomicBoolean excess) {
        return new MealTo(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
