package com.nibado.project.grub.meals.service.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class Portion {
    private UUID ingredientId;
    private String name;
    private double fraction;
}
