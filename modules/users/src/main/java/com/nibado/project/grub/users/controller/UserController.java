package com.nibado.project.grub.users.controller;

import com.nibado.project.grub.users.controller.dto.LoginRequest;
import com.nibado.project.grub.users.controller.dto.LoginResponse;
import com.nibado.project.grub.users.controller.dto.UserListResponse;
import com.nibado.project.grub.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    @Autowired
    private UserController(final UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest login) {
        String token = service.login(login.getEmail(), login.getPassword());
        return new LoginResponse(token, ZonedDateTime.now().plusDays(7));
    }

    @GetMapping
    public UserListResponse getAll() {
        return UserListResponse.of(service.getAll());
    }
}
