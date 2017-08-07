package com.nibado.project.grub.meals.controller.dto;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class IngredientListDTO {
    private final List<IngredientDTO> ingredients;

    public static IngredientListDTO of(final List<Ingredient> ingredients) {
        return new IngredientListDTO(ingredients.stream().map(IngredientDTO::of).collect(Collectors.toList()));
    }
}
