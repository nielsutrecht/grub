package com.nibado.project.grub.diary.repository;

import com.nibado.project.grub.common.Dates;
import com.nibado.project.grub.diary.repository.domain.MealTime;
import com.nibado.project.grub.diary.repository.domain.UserMeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nibado.project.grub.common.Dates.toLocalDate;
import static java.util.UUID.fromString;

@Repository
public class UserMealRepository {
    private static final RowMapper<UserMeal> MAPPER = (rs, rowNum) ->
            new UserMeal(
                    fromString(rs.getString("user_id")),
                    fromString(rs.getString("meal_id")),
                    toLocalDate(rs.getDate("date")),
                    MealTime.valueOf(rs.getString("time")),
                    rs.getDouble("amount"));

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserMealRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UserMeal> getMeal(final UUID userId, final UUID mealId, final LocalDate date, final MealTime time) {
        return jdbcTemplate.query(
                "SELECT user_id, meal_id, date, time, amount FROM user_meals WHERE user_id = ? AND meal_id = ? AND date = ? AND time = ?",
                MAPPER,
                userId,
                mealId,
                date,
                time.toString()
        ).stream().findAny();
    }

    public List<UserMeal> getMeals(final UUID userId) {
        return jdbcTemplate.query(
                "SELECT user_id, meal_id, date, time, amount FROM user_meals WHERE user_id = ? ORDER BY date DESC",
                MAPPER,
                userId
        );
    }

    public List<UserMeal> getMeals(final UUID userId, final LocalDate date) {
        return jdbcTemplate.query(
                "SELECT user_id, meal_id, date, time, amount FROM user_meals WHERE user_id = ? AND date = ? ORDER BY date DESC",
                MAPPER,
                userId,
                date
        );
    }

    public List<UserMeal> getMeals(final UUID userId, final LocalDate dateFrom, final LocalDate dateTo) {
        if(dateTo.isBefore(dateFrom)) {
            throw new IllegalArgumentException("From can't be before To");
        }
        return jdbcTemplate.query(
                "SELECT user_id, meal_id, date, time, amount FROM user_meals WHERE user_id = ? AND date >= ? AND date <= ? ORDER BY date DESC",
                MAPPER,
                userId,
                dateFrom,
                dateTo
        );
    }

    public void create(final UUID userId, final UUID mealId, final LocalDate date, final MealTime time, final double amount) {
        jdbcTemplate.update(
                "INSERT INTO user_meals (user_id, meal_id, date, time, amount) VALUES(?,?,?,?,?)",
                userId,
                mealId,
                Dates.toString(date),
                time.toString(),
                amount);
    }

}
