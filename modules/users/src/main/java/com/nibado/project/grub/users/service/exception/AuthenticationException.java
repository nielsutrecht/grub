package com.nibado.project.grub.users.service.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(final String message) {
        super(message);
    }
}
