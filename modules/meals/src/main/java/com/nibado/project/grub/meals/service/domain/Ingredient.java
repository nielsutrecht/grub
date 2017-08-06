package com.nibado.project.grub.meals.service.domain;

import lombok.Value;

import java.util.Map;

@Value
public class Ingredient {
    private final String name;
    private final int kiloCalories;
    private final Map<String, Portion> portions;
}
