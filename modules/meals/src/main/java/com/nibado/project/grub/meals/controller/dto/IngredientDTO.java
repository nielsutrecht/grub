package com.nibado.project.grub.meals.controller.dto;

import com.nibado.project.grub.meals.service.domain.Ingredient;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class IngredientDTO {
    private final UUID id;
    private final String name;
    private final int kiloCalories;
    private final List<PortionDTO> portions;

    public static IngredientDTO of(final Ingredient ingredient) {
        List<PortionDTO> portions = ingredient.getPortions().values().stream().map(PortionDTO::of).collect(Collectors.toList());

        return new IngredientDTO(ingredient.getId(), ingredient.getName(), ingredient.getKiloCalories(), portions);
    }
}
