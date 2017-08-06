package com.nibado.project.grub.controller.dto;

import lombok.Value;

import java.util.List;

@Value
public class MealListResponse {
    private List<MealDto> meals;
}
