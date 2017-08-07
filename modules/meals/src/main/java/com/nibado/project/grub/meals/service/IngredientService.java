package com.nibado.project.grub.meals.service;

import com.nibado.project.grub.meals.importer.Importer;
import com.nibado.project.grub.meals.repository.IngredientRepository;
import com.nibado.project.grub.meals.repository.PortionRepository;
import com.nibado.project.grub.meals.service.domain.Ingredient;
import com.nibado.project.grub.meals.service.domain.Portion;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final PortionRepository portionRepository;

    public IngredientService(final IngredientRepository ingredientRepository, final PortionRepository portionRepository) {
        this.ingredientRepository = ingredientRepository;
        this.portionRepository = portionRepository;
    }

    public List<Ingredient> searchByName(final String name) {
        List<Ingredient> ingredients;
        if (name == null || name.trim().isEmpty()) {
            ingredients = ingredientRepository.getAll();
        } else {
            ingredients = ingredientRepository.searchByName(name);
        }
        ingredients.forEach(this::addPortions);
        return ingredients;
    }

    private void addPortions(final Ingredient ingredient) {
        ingredient.getPortions().put("unit", new Portion(null, "unit", 1.0));
        ingredient.getPortions().put("gram", new Portion(null, "gram", 0.01));

        portionRepository.get(ingredient).forEach(p -> ingredient.getPortions().put(p.getName(), p));
    }

    @PostConstruct
    private void init() throws Exception {
        Importer.importMeals().getIngredients().forEach(this::insert);
    }

    private void insert(final Importer.Ingredient ingredient) {
        Ingredient i = ingredientRepository.create(ingredient.getName(), ingredient.getKcal());

        if (ingredient.getPortion() != null) {
            ingredient.getPortion().forEach(p -> portionRepository.create(i, p.getName(), p.getFrac()));
        }
    }
}
