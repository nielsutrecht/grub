package com.nibado.project.cli.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private final String email;
    private final String password;
}
