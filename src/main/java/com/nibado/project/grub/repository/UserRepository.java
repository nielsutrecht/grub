package com.nibado.project.grub.repository;

import com.nibado.project.grub.repository.domain.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public Optional<User> findByEmail(final String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findAny();
    }

    public void createUser(final String email, final String name, final String password) {
        users.add(new User(email, name, password));
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    @PostConstruct
    public void init() {
        createUser("ndommerholt@gmail.com", "Niels", "foo");
        createUser("efkranveld@gmail.com", "Fleur", "bar");
    }
}
