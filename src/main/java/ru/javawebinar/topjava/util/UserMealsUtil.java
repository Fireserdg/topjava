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
        System.out.println("Exceeded with ForEach: " + exceeded);
        System.out.println("Exceeded with Stream API: " + exceededStream);
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
        for (UserMeal userMeal : mealList) {
            map.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(),
                    (oldVal, newVal) -> oldVal + userMeal.getCalories());
        }
        mealList.sort(Comparator.comparing(UserMeal::getDateTime));
        List<UserMealWithExceed> list = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExceed(userMeal.getDateTime(),
                        userMeal.getDescription(), userMeal.getCalories(),
                        map.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
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
        Map<LocalDate, Integer> map = mealList.stream().collect(Collectors.toMap(
                k -> k.getDateTime().toLocalDate(), UserMeal::getCalories, (v1, v2) -> v1 + v2));
        return mealList.stream().sorted(Comparator.comparing(UserMeal::getDateTime)).filter(
                meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(v -> new UserMealWithExceed(v.getDateTime(), v.getDescription(), v.getCalories(),
                        map.get(v.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
    }
}
