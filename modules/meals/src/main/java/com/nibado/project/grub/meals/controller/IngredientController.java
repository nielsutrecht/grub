package com.nibado.project.grub.meals.controller;

import com.nibado.project.grub.meals.controller.dto.IngredientListDTO;
import com.nibado.project.grub.meals.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService service;

    @Autowired
    public IngredientController(final IngredientService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public IngredientListDTO search(@RequestParam(value = "name", required = false)final String name) {
        return IngredientListDTO.of(service.searchByName(name));
    }
}
