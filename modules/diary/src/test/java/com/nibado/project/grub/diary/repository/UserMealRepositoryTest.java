package com.nibado.project.grub.diary.repository;

import com.nibado.project.grub.common.Dates;
import com.nibado.project.grub.diary.repository.domain.MealTime;
import com.nibado.project.grub.diary.repository.domain.UserMeal;
import com.nibado.project.grub.repository.RepositoryTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserMealRepositoryTest extends RepositoryTest<UserMeal> {
    private static final UUID USER_0 = new UUID(0, 0);
    private static final UUID USER_1 = new UUID(0, 1);
    private static final UUID USER_NA = new UUID(0, 2);
    private static final UUID MEAL_ID = new UUID(1, 1);
    private static final LocalDate DATE = LocalDate.of(2001, 2, 3);

    private UserMealRepository repository;

    public UserMealRepositoryTest() {
        super("user_meals");
    }

    @Before
    public void setup() {
        super.setup();
        template.update("INSERT INTO users(id) VALUES(?)", USER_0);
        template.update("INSERT INTO users(id) VALUES(?)", USER_1);
        template.update("INSERT INTO meals(id) VALUES(?)", MEAL_ID);

        repository = new UserMealRepository(template);
    }

    @Test
    public void create() throws Exception {
        repository.create(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0);

        assertRows(1);

        Map<String, Object> inserted = singleRow();

        assertThat(inserted.get("USER_ID")).isEqualTo(USER_0);
        assertThat(inserted.get("MEAL_ID")).isEqualTo(MEAL_ID);
        assertThat(Dates.toLocalDate((java.sql.Date) inserted.get("DATE"))).isEqualTo(DATE);
        assertThat(inserted.get("TIME")).isEqualTo("DINNER");
        assertThat(inserted.get("AMOUNT")).isEqualTo(2.0);
    }

    @Test
    public void getMeal() throws Exception {
        repository.create(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0);
        assertRows(1);

        Optional<UserMeal> meal = repository.getMeal(USER_0, MEAL_ID, DATE, MealTime.DINNER);

        assertThat(meal).isPresent();
        assertThat(meal).contains(new UserMeal(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0));
    }

    @Test
    public void getMeal_Empty() throws Exception {
        Optional<UserMeal> meal = repository.getMeal(USER_0, MEAL_ID, DATE, MealTime.DINNER);

        assertThat(meal).isEmpty();
    }

    @Test
    public void getMeals_ForUser() {
        repository.create(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE.plusDays(1), MealTime.LUNCH, 1.0);
        repository.create(USER_1, MEAL_ID, DATE, MealTime.DINNER, 2.0);
        assertRows(3);
        List<UserMeal> meals = repository.getMeals(USER_0);

        assertThat(meals).hasSize(2);
        assertThat(meals).extracting("userId").containsOnly(USER_0);
        assertThat(meals).extracting("mealId").containsOnly(MEAL_ID);
        assertThat(meals).extracting("date").containsExactly(DATE.plusDays(1), DATE);
        assertThat(meals).extracting("time").containsExactly(MealTime.LUNCH, MealTime.DINNER);
        assertThat(meals).extracting("amount").containsExactly(1.0, 2.0);

        meals = repository.getMeals(USER_1);

        assertThat(meals).hasSize(1);

        meals = repository.getMeals(USER_NA);

        assertThat(meals).isEmpty();
    }

    @Test
    public void getMeals_ForUserAndDate() {
        repository.create(USER_0, MEAL_ID, DATE.plusDays(-1), MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE.plusDays(-1), MealTime.LUNCH, 2.0);

        repository.create(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE, MealTime.LUNCH, 2.0);

        repository.create(USER_0, MEAL_ID, DATE.plusDays(1), MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE.plusDays(1), MealTime.LUNCH, 2.0);

        assertRows(6);

        List<UserMeal> meals = repository.getMeals(USER_0, DATE);

        assertThat(meals).hasSize(2);
        assertThat(meals).extracting("userId").containsOnly(USER_0);
        assertThat(meals).extracting("mealId").containsOnly(MEAL_ID);
        assertThat(meals).extracting("date").containsOnly(DATE);
        assertThat(meals).extracting("time").containsExactly(MealTime.DINNER, MealTime.LUNCH);
        assertThat(meals).extracting("amount").containsExactly(2.0, 2.0);

        meals = repository.getMeals(USER_0, DATE.plusDays(1));
        assertThat(meals).hasSize(2);
        assertThat(meals).extracting("date").containsOnly(DATE.plusDays(1));

        meals = repository.getMeals(USER_0, DATE.plusDays(-1));
        assertThat(meals).hasSize(2);
        assertThat(meals).extracting("date").containsOnly(DATE.plusDays(-1));

        meals = repository.getMeals(USER_0, DATE.plusDays(2));
        assertThat(meals).isEmpty();
    }

    @Test
    public void getMeals_ForUserAndDateRange() {
        repository.create(USER_0, MEAL_ID, DATE.plusDays(-1), MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE.plusDays(-1), MealTime.LUNCH, 2.0);

        repository.create(USER_0, MEAL_ID, DATE, MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE, MealTime.LUNCH, 2.0);

        repository.create(USER_0, MEAL_ID, DATE.plusDays(1), MealTime.DINNER, 2.0);
        repository.create(USER_0, MEAL_ID, DATE.plusDays(1), MealTime.LUNCH, 2.0);

        assertRows(6);

        List<UserMeal> meals = repository.getMeals(USER_0, DATE.plusDays(-1), DATE);

        assertThat(meals).hasSize(4);
        assertThat(meals).extracting("userId").containsOnly(USER_0);
        assertThat(meals).extracting("mealId").containsOnly(MEAL_ID);
        assertThat(meals).extracting("date").containsOnly(DATE, DATE.plusDays(-1));
        assertThat(meals).extracting("time").containsOnly(MealTime.DINNER, MealTime.LUNCH);
        assertThat(meals).extracting("amount").containsOnly(2.0);

        meals = repository.getMeals(USER_0, DATE.plusDays(1), DATE.plusDays(2));
        assertThat(meals).hasSize(2);

        meals = repository.getMeals(USER_0, DATE.plusDays(2), DATE.plusDays(5));
        assertThat(meals).hasSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMeals_ForUserAndDateRange_IllegalRange() {
        repository.getMeals(USER_0, DATE, DATE.plusDays(-1));
    }
}
