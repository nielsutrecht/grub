package com.nibado.project.grub.meals.service.domain;

import lombok.Value;

import java.util.List;

@Value
public class Meal {
    private final String name;
    private final List<MealIngredient> ingredients;
    private final int totalKiloCalories;
}
