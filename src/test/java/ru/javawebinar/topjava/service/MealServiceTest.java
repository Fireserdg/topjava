package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static ru.javawebinar.topjava.TestData.*;

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
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, 3, 4, 13, 0), "newEat", 600);
        Meal created = service.create(newMeal, ADMIN_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(ADMIN_ID), newMeal, TWO_MEAL, ONE_MEAL);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_ONE_ID, USER_ID);
        assertMatch(service.getAll(ADMIN_ID), TWO_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, 1);
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_ONE_ID, ADMIN_ID);
        assertMatch(meal, ONE_MEAL);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, 1);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(ONE_MEAL);
        updated.setCalories(100);
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(MEAL_ONE_ID, ADMIN_ID), updated);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(ONE_MEAL);
        updated.setCalories(100);
        service.update(updated, 3);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, TWO_MEAL, ONE_MEAL);
    }

    @Test
    public void getBetween() throws Exception {
        List<Meal> meals = service.getBetweenDates(
                LocalDate.of(2015, 5, 30),
                LocalDate.of(2015, 5, 30),
                USER_ID);
        assertThat(meals).isEqualTo(MEALS_USER);
    }
}