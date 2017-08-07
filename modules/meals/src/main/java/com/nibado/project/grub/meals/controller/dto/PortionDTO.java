package com.nibado.project.grub.meals.controller.dto;

import com.nibado.project.grub.meals.service.domain.Portion;
import lombok.Data;

@Data
public class PortionDTO {
    private final String name;
    private final double fraction;

    public static PortionDTO of(final Portion portion) {
        return new PortionDTO(portion.getName(), portion.getFraction());
    }
}
