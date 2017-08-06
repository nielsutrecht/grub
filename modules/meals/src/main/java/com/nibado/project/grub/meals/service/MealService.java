package com.nibado.project.grub.meals.service;

import com.nibado.project.grub.meals.importer.Importer;
import com.nibado.project.grub.meals.service.domain.Ingredient;
import com.nibado.project.grub.meals.service.domain.Meal;
import com.nibado.project.grub.meals.service.domain.MealIngredient;
import com.nibado.project.grub.meals.service.domain.Portion;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MealService {
    private List<Meal> meals;
    private Map<String, Ingredient> ingredients;

    public Collection<Meal> allMeals() {
        return meals;
    }

    public Collection<Ingredient> allIngredients() {
        return ingredients.values();
    }

    @PostConstruct
    private void init() throws Exception {
        Importer.Result result = Importer.importMeals();

        ingredients = result.getIngredients().stream().map(MealService::map).collect(Collectors.toMap(Ingredient::getName, i -> i));
        meals = result.getMeals().stream().map(this::map).collect(Collectors.toList());
    }

    private static Ingredient map(Importer.Ingredient ingredient) {
        Map<String, Portion> portions = new HashMap<>();

        portions.put("unit", new Portion("100g", 1.0));
        portions.put("gram", new Portion("gram", 0.01));

        if (ingredient.getPortion() != null) {
            ingredient.getPortion()
                    .forEach(p -> portions.put(p.getName(), map(p)));
        }

        return new Ingredient(ingredient.getName(), ingredient.getKcal(), portions);
    }

    private static Portion map(Importer.IngredientPortion portion) {
        return new Portion(portion.getName(), portion.getFrac());
    }

    private Meal map(Importer.Meal meal) {
        List<MealIngredient> mealIngredients = meal.getIngredients().stream().map(this::map).collect(Collectors.toList());
        return new Meal(meal.getName(), mealIngredients, total(mealIngredients));
    }

    private static int total(final List<MealIngredient> mealIngredients) {
        return mealIngredients
                .stream()
                .mapToInt(i -> (int) (i.getAmount() * i.getPortion().getFraction() * i.getIngredient().getKiloCalories()))
                .sum();
    }

    private MealIngredient map(Importer.MealIngredient mealIngredient) {
        Ingredient ingredient = ingredients.get(mealIngredient.getName());
        Portion portion = ingredient.getPortions().get(mealIngredient.getPortion());

        return new MealIngredient(ingredient, portion, mealIngredient.getAmount());
    }
}
