package com.nibado.project.grub.controller.dto;

import com.nibado.project.grub.repository.domain.User;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class UserListResponse {
    private final List<UserListEntry> users;

    public static UserListResponse of(final List<User> users) {
        return new UserListResponse(users
                .stream()
                .map(u -> new UserListEntry(u.getName(), u.getEmail()))
                .collect(Collectors.toList()));
    }

    @Value
    public static class UserListEntry {
        private final String name;
        private final String email;
    }
}
