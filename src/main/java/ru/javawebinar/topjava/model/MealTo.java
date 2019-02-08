package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class MealTo {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private boolean exceed;

    private AtomicBoolean exceedAtomic;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
    }

    public MealTo(LocalDateTime dateTime, String description, int calories, AtomicBoolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceedAtomic = exceed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "MealTo{dateTime=%s, description=%s, calories=%s, exceed=", dateTime, description, calories));
        if (Objects.nonNull(exceedAtomic)) {
            sb.append(exceedAtomic.get()).append("}");
        } else {
            sb.append(exceed).append("}");
        }
        return sb.toString();
    }
}
