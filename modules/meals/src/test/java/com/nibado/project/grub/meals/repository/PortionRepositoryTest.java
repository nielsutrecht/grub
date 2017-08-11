package com.nibado.project.grub.meals.repository;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import com.nibado.project.grub.meals.service.domain.Portion;
import com.nibado.project.grub.repository.RepositoryTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PortionRepositoryTest extends RepositoryTest<Ingredient> {
    private IngredientRepository ingredientRepository;
    private PortionRepository repository;
    private Ingredient ingredientA;
    private Ingredient ingredientB;

    public PortionRepositoryTest() {
        super("portions");
    }

    @Before
    public void setup() {
        super.setup();
        repository = new PortionRepository(template);
        ingredientRepository = new IngredientRepository(template);

        JdbcTestUtils.deleteFromTables(template, "ingredients");

        ingredientA = ingredientRepository.create("A", 100);
        ingredientB = ingredientRepository.create("B", 200);
    }

    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    public void create() throws Exception {
        repository.create(ingredientA, "foo", 0.15);
        assertRows(1);
        repository.create(ingredientA, "bar", 0.15);
        assertRows(2);
    }

    @Test
    public void get() throws Exception {
        repository.create(ingredientA, "foo", 0.15);
        repository.create(ingredientA, "bar", 0.30);
        repository.create(ingredientB, "foo", 0.45);
        repository.create(ingredientB, "bar", 0.60);

        List<Portion> portions = repository.get(ingredientA);

        assertThat(portions).hasSize(2)
                .extracting("ingredientId").containsOnly(ingredientA.getId());
        assertThat(portions).extracting("name").containsExactly("bar", "foo");
        assertThat(portions).extracting("fraction").containsExactly(0.30, 0.15);
    }

    @Test
    public void delete() throws Exception {
        repository.create(ingredientA, "foo", 0.15);
        assertRows(1);
        repository.delete(ingredientA, "foo");
        assertEmpty();
    }

    @Test
    public void deleteAll() throws Exception {
        repository.create(ingredientA, "foo", 0.15);
        repository.create(ingredientA, "bar", 0.15);
        assertRows(2);
        repository.deleteAll(ingredientA);
        assertEmpty();
    }
}
