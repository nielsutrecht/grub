package com.nibado.project.grub.controller.dto;

import com.nibado.project.grub.service.domain.Meal;
import com.nibado.project.grub.service.domain.MealIngredient;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class MealDto {
    private String name;
    private int totalKiloCalories;
    private List<MealIngredientDto> ingredients;

    public static MealDto of(final Meal meal) {
        List<MealIngredientDto> ingredients = meal.getIngredients()
                .stream()
                .map(MealIngredientDto::of)
                .collect(Collectors.toList());

        return new MealDto(meal.getName(), meal.getTotalKiloCalories(), ingredients);
    }

    @Value
    public static class MealIngredientDto {
        private String name;
        private String portion;
        private int amount;
        private int kiloCalories;
        private int subTotalKiloCalories;
        private double fraction;

        public static MealIngredientDto of(final MealIngredient ingredient) {
            int subtotal = (int) (ingredient.getAmount() * ingredient.getPortion().getFraction() * ingredient.getIngredient().getKiloCalories());

            return new MealIngredientDto(
                    ingredient.getIngredient().getName(),
                    ingredient.getPortion().getName(),
                    ingredient.getAmount(),
                    ingredient.getIngredient().getKiloCalories(),
                    subtotal,
                    ingredient.getPortion().getFraction());
        }
    }
}
