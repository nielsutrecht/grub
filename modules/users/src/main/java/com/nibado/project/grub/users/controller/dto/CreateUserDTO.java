package com.nibado.project.grub.users.controller.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String email;
    private String name;
    private String password;
    private boolean isAdmin;
}
