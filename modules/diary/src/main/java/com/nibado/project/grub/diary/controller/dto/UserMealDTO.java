package com.nibado.project.grub.diary.controller.dto;

import com.nibado.project.grub.diary.repository.domain.MealTime;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class UserMealDTO {
    private String name;
    private UUID mealId;
    private LocalDate date;
    private MealTime time;
    private double amount;
    private int kiloCalories;
}
