package com.nibado.project.grub.users.controller.dto;

import com.nibado.project.grub.users.repository.domain.User;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class UserListResponse {
    private final List<UserDTO> users;

    public static UserListResponse of(final List<User> users) {
        return new UserListResponse(users
                .stream()
                .map(UserDTO::of)
                .collect(Collectors.toList()));
    }
}
