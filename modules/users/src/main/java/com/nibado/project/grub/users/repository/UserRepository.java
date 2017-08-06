package com.nibado.project.grub.users.repository;

import com.nibado.project.grub.users.repository.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public class UserRepository {
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(rs.getString("email"), rs.getString("name"), rs.getString("password"));
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(final String email) {
        return jdbcTemplate
                .query("SELECT email, name, password FROM users WHERE email = ?", USER_ROW_MAPPER, email)
                .stream()
                .findAny();
    }

    public void createUser(final String email, final String name, final String password) {
        jdbcTemplate.update("INSERT INTO users (email, name, password) VALUES(?,?,?)", email, name, password);
    }

    public void deleteUser(final String email) {
        jdbcTemplate.update("DELETE FROM users WHERE email = ?", email);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT email, name, password FROM users", USER_ROW_MAPPER);
    }
}
