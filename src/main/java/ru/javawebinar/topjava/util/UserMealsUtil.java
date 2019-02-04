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
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> exceeded = getFilteredWithExceeded(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExceed> exceededStream = getFilteredWithExceededWithStream(
                mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
       exceeded.forEach(System.out::println);
       exceededStream.forEach(System.out::println);

}

    /**
     * Use forEach
     *
     * @param mealList       mealList
     * @param startTime      startTime
     * @param endTime        endTime
     * @param caloriesPerDay caloriesPerDay
     * @return list of UserMealWithExceed
     */
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        mealList.forEach((meal) ->  map.merge(
                meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        List<UserMealWithExceed> list = new ArrayList<>();
        mealList.forEach((meal) -> {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExceed(meal.getDateTime(),
                        meal.getDescription(), meal.getCalories(),
                        map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return list;
    }

    /**
     * Use Stream API Java 8
     *
     * @param mealList       mealList
     * @param startTime      startTime
     * @param endTime        endTime
     * @param caloriesPerDay caloriesPerDay
     * @return list of UserMealWithExceed
     */
    public static List<UserMealWithExceed> getFilteredWithExceededWithStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = mealList.stream().
                collect(Collectors.toMap(k -> k.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(v -> new UserMealWithExceed(v.getDateTime(), v.getDescription(), v.getCalories(),
                        map.get(v.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
