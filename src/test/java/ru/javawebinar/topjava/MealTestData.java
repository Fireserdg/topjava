package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static java.time.LocalDateTime.of;

public class MealTestData {

    public static final int MEAL_ONE_ID = START_SEQ + 8;
    public static final int MEAL_TWO_ID = START_SEQ + 9;

    public static final AtomicInteger count = new AtomicInteger(ADMIN_ID);

    public static final List<Meal> MEALS_USER = MealsUtil.MEALS.stream().limit(3)
            .peek(meal -> meal.setId(count.incrementAndGet()))
            .sorted(Comparator.comparing(Meal::getDateTime)
                    .reversed()).collect(Collectors.toList());

    public static final Meal ONE_MEAL = new Meal(MEAL_ONE_ID, of(
            2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal TWO_MEAL = new Meal(MEAL_TWO_ID, of(
            2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("description").isEqualTo(expected);
    }
}
