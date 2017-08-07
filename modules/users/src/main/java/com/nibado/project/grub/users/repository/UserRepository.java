package com.nibado.project.grub.users.repository;

import com.nibado.project.grub.users.repository.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@Slf4j
public class UserRepository {
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getBoolean("admin"));
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(final String email) {
        return jdbcTemplate
                .query("SELECT id, email, name, password, admin FROM users WHERE email = ?", USER_ROW_MAPPER, email)
                .stream()
                .findAny();
    }

    public void createUser(final String email, final String name, final String password, final boolean admin) {
        jdbcTemplate.update(
                "INSERT INTO users (id, email, name, password, admin) VALUES(?,?,?,?,?)",
                UUID.randomUUID(),
                email,
                name,
                password,
                admin);
    }

    public void updatePassword(final String email, final String password) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ?", password, email);
    }

    public void deleteUser(final String email) {
        jdbcTemplate.update("DELETE FROM users WHERE email = ?", email);
    }

    public int count() {
        return jdbcTemplate.queryForObject("SELECT count(id) FROM users", Integer.class);
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT id, email, name, password, admin FROM users", USER_ROW_MAPPER);
    }
}
