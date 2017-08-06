package com.nibado.project.grub.users.controller.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
