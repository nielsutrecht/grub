package com.nibado.project.grub.users.controller.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LoginResponse {
    private final String token;
    private final ZonedDateTime expires;
}
