package com.nibado.project.grub.diary.controller.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserMealsDTO {
    private List<UserMealDTO> meals;
}
