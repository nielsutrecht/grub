package com.nibado.project.grub.users.controller.dto;

import com.nibado.project.grub.users.repository.domain.User;
import lombok.Data;

@Data
public class UserDTO {
    private final String name;
    private final String email;
    private final boolean admin;

    public static UserDTO of(final User user) {
        return new UserDTO(user.getName(), user.getEmail(), user.isAdmin());
    }
}
