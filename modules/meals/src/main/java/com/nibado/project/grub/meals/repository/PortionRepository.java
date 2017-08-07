package com.nibado.project.grub.meals.repository;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import com.nibado.project.grub.meals.service.domain.Portion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PortionRepository {
    private static final RowMapper<Portion> MAPPER = (rs, rowNum) ->
            new Portion(
                    UUID.fromString(rs.getString("ingredient_id")),
                    rs.getString("name"),
                    rs.getDouble("fraction"));

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PortionRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(final Ingredient ingredient, final String name, final double fraction) {
        create(ingredient.getId(), name, fraction);
    }

    public void create(final UUID ingredientId, final String name, final double fraction) {
        jdbcTemplate.update("INSERT INTO portions(ingredient_id, name, fraction) VALUES (?,?,?)", ingredientId, name, fraction);
    }

    public List<Portion> get(final Ingredient ingredient) {
        return get(ingredient.getId());
    }

    public List<Portion> get(final UUID ingredientId) {
        return jdbcTemplate.query("SELECT ingredient_id, name, fraction FROM portions WHERE ingredient_id = ?", MAPPER, ingredientId);
    }

    public void delete(final Ingredient ingredient, final String name) {
        delete(ingredient.getId(), name);
    }

    public void delete(final UUID ingredientId, final String name) {
        jdbcTemplate.update("DELETE FROM portions WHERE ingredient_id = ? AND name = ?", ingredientId, name);
    }

    public void deleteAll(final Ingredient ingredient) {
        deleteAll(ingredient.getId());
    }

    public void deleteAll(final UUID ingredientId) {
        jdbcTemplate.update("DELETE FROM portions WHERE ingredient_id = ?", ingredientId);
    }
}
