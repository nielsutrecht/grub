package com.nibado.project.grub.users.repository.domain;

import lombok.Value;

import java.util.UUID;

@Value
public class User {
    private final UUID id;
    private final String email;
    private final String name;
    private final String password;
    private final boolean admin;

    public boolean isAuthorized() {
        return email != null;
    }
}
