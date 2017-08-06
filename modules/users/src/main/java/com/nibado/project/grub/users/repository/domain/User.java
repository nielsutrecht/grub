package com.nibado.project.grub.users.repository.domain;

import lombok.Value;

@Value
public class User {
    private final String email;
    private final String name;
    private final String password;

    public boolean isAuthorized() {
        return email != null;
    }
}
