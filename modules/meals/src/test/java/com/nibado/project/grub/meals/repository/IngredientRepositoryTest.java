package com.nibado.project.grub.meals.repository;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import com.nibado.project.grub.repository.RepositoryTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientRepositoryTest extends RepositoryTest<Ingredient> {
    private IngredientRepository repository;
    public IngredientRepositoryTest() {
        super("ingredients");
    }

    @Before
    public void setup() {
        super.setup();
        repository = new IngredientRepository(template);
    }

    @After
    public void teardown() {
        super.teardown();
    }

    @Test
    public void create() throws Exception {
        repository.create("A", 100);
        repository.create("B", 200);

        assertRows(2);
    }

    @Test
    public void findByName() throws Exception {
        repository.create("Test", 100);
        repository.create("ATest", 100);
        repository.create("TestB", 100);
        repository.create("CTestC", 100);
        repository.create("DtestD", 100);
        repository.create("ETes", 100);

        List<Ingredient> results = repository.searchByName("Test");

        assertThat(results)
                .hasSize(5)
                .extracting("name")
                .containsOnly("Test", "ATest", "TestB", "CTestC", "DtestD");
    }
}
