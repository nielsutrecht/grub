package com.nibado.project.grub.users.service;

import com.nibado.project.grub.users.components.Hash;
import com.nibado.project.grub.users.components.Jwts;
import com.nibado.project.grub.users.repository.UserRepository;
import com.nibado.project.grub.users.repository.domain.User;
import com.nibado.project.grub.users.service.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository repository;
    private final Jwts jwts;

    @Autowired
    public UserService(final UserRepository repository, final Jwts jwts) {
        this.repository = repository;
        this.jwts = jwts;
    }

    public String login(final String email, final String password) {
        User user = repository.findByEmail(email).orElseThrow(() -> new AuthenticationException("User not found"));

        if (!Hash.validate(password, user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        log.info("Logged in user {}", email);

        return jwts.createToken(user);
    }

    public void createUser(final String email, final String name, final String password) {
        String hash = Hash.create(password);

        repository.createUser(email, name, hash);

        log.info("Created user {}: {}", email, name);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    @PostConstruct
    public void init() {
        createUser("ndommerholt@gmail.com", "Niels", "foo");
        createUser("efkraneveld@hotmail.com", "Fleur", "bar");

        repository.findAll().forEach(u -> log.info("{}", u));
    }
}
