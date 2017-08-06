package com.nibado.project.grub.users.service;

import com.nibado.project.grub.users.repository.UserRepository;
import com.nibado.project.grub.users.repository.domain.User;
import com.nibado.project.grub.users.service.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final JwtsService jwtsService;

    @Autowired
    public UserService(final UserRepository repository, final JwtsService jwtsService) {
        this.repository = repository;
        this.jwtsService = jwtsService;
    }

    public String login(final String email, final String password) {
        User user = repository.findByEmail(email).orElseThrow(() -> new AuthenticationException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid password");
        }

        return jwtsService.createToken(user);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
