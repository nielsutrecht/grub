package com.nibado.project.grub.meals.service.domain;

import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
public class Ingredient {
    private final UUID id;
    private final String name;
    private final int kiloCalories;
    private final Map<String, Portion> portions;
}
