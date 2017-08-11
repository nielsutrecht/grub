package com.nibado.project.grub.diary.controller;

import com.nibado.project.grub.diary.controller.dto.UserMealDTO;
import com.nibado.project.grub.diary.controller.dto.UserMealsDTO;
import com.nibado.project.grub.diary.repository.domain.MealTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @GetMapping("/me")
    public UserMealsDTO meals() {
        return new UserMealsDTO(
                Arrays.asList(
                        new UserMealDTO("Test Dinner", new UUID(0, 0), LocalDate.now(), MealTime.DINNER, 1.0, 100),
                        new UserMealDTO("Test Lunch", new UUID(0, 0), LocalDate.now(), MealTime.LUNCH, 1.0, 200),
                        new UserMealDTO("Test Breakfast", new UUID(0, 0), LocalDate.now(), MealTime.BREAKFAST, 1.0, 300)
                )
        );
    }
}
