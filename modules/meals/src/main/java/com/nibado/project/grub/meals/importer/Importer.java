package com.nibado.project.grub.meals.importer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Importer {
    public static Result importMeals() throws Exception {
        YAMLMapper mapper = new YAMLMapper();

        IngredientList iList = mapper.readValue(Importer.class.getResourceAsStream("/data/ingredients.yml"), IngredientList.class);
        MealList mList = mapper.readValue(Importer.class.getResourceAsStream("/data/meals.yml"), MealList.class);

        Map<String, Ingredient> ingredients = new HashMap<>();

        for (Ingredient i : iList.getIngredients()) {
            i.getPortionMap().put("unit", new IngredientPortion("100g", 1.0));
            i.getPortionMap().put("gram", new IngredientPortion("gram", 0.01));

            if (i.getPortion() != null) {
                for (IngredientPortion p : i.getPortion()) {
                    i.getPortionMap().put(p.getName(), p);
                }
            }

            ingredients.put(i.getName(), i);
        }

        for (Meal meal : mList.getMeals()) {
            for (MealIngredient mealIngredient : meal.getIngredients()) {
                if (mealIngredient.getPortion() == null) {
                    mealIngredient.setPortion("unit");
                }
                if (mealIngredient.getAmount() == 0) {
                    mealIngredient.setAmount(1);
                }


                Ingredient ing = ingredients.get(mealIngredient.getName());

                if (ing == null) {
                    log.error("Ingredient {} in meal {} does not exist", mealIngredient.getName(), meal.getName());
                    throw new RuntimeException();
                }

                if (!ing.getPortionMap().containsKey(mealIngredient.getPortion())) {
                    log.error("Ingredient {} in meal {} does not have portion {}", mealIngredient.getName(), meal.getName(), mealIngredient.getPortion());
                    throw new RuntimeException();
                }
            }
        }

        return new Result(iList.getIngredients(), mList.getMeals());
    }

    @Data
    public static class Result {
        private final List<Ingredient> ingredients;
        private final List<Meal> meals;
    }

    @Data
    public static class IngredientList {
        private List<Ingredient> ingredients;
    }

    @Data
    @ToString
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Ingredient {
        private String name;
        private int kcal;
        private List<IngredientPortion> portion;
        private Map<String, IngredientPortion> portionMap = new HashMap<>();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientPortion {
        private String name;
        private double frac;
    }

    @Data
    public static class MealList {
        private List<Meal> meals;
    }

    @Data
    public static class Meal {
        private String name;
        private List<MealIngredient> ingredients;
    }

    @Data
    public static class MealIngredient {
        private String name;
        private int amount;
        private String portion;
    }
}
