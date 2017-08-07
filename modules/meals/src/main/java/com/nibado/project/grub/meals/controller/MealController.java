package com.nibado.project.grub.meals.controller;


import com.nibado.project.grub.meals.controller.dto.MealDto;
import com.nibado.project.grub.meals.controller.dto.MealListResponse;
import com.nibado.project.grub.meals.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meal")
public class MealController {
    private final MealService service;

    @Autowired
    public MealController(final MealService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public MealListResponse getAll() {
        List<MealDto> meals = service.allMeals().stream().map(MealDto::of).collect(Collectors.toList());
        return new MealListResponse(meals);
    }
}
