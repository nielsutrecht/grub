package com.nibado.project.grub.meals.repository;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class IngredientRepository {
    private static final RowMapper<Ingredient> MAPPER = (rs, rowNum) ->
            new Ingredient(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("name"),
                    rs.getInt("kiloCalories"), new HashMap<>());

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IngredientRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Ingredient create(final String name, final int kiloCalories) {
        Ingredient i = new Ingredient(UUID.randomUUID(), name, kiloCalories, new HashMap<>());

        jdbcTemplate.update("INSERT INTO ingredients(id, name, kiloCalories) VALUES (?,?,?)", i.getId(), name, kiloCalories);

        return i;
    }

    public List<Ingredient> searchByName(final String name) {
        return jdbcTemplate
                .query("SELECT id, name, kiloCalories FROM ingredients WHERE REGEXP_LIKE(name, ?, 'i') ORDER BY name", MAPPER, name);
    }

    public List<Ingredient> getAll() {
        return jdbcTemplate
                .query("SELECT id, name, kiloCalories FROM ingredients", MAPPER);
    }
}
