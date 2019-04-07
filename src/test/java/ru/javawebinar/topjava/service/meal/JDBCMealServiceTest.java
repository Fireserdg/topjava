package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.meal.AbstractMealServiceTest;


@ActiveProfiles(Profiles.JDBC)
public class JDBCMealServiceTest extends AbstractMealServiceTest {
}
