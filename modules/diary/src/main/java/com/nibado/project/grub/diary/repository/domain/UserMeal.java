package com.nibado.project.grub.diary.repository.domain;

import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class UserMeal {
    private UUID userId;
    private UUID mealId;
    private LocalDate date;
    private MealTime time;
    private double amount;
}
