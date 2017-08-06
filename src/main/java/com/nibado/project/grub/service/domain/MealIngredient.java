package com.nibado.project.grub.service.domain;

import lombok.Value;

@Value
public class MealIngredient {
    private Ingredient ingredient;
    private Portion portion;
    private int amount;
}
