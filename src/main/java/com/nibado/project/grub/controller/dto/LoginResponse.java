package com.nibado.project.grub.controller.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LoginResponse {
    private final String token;
    private final ZonedDateTime expires;
}
