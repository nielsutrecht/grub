package com.nibado.project.grub.users.controller;

import com.nibado.project.grub.access.Access;
import com.nibado.project.grub.access.AccessLevel;
import com.nibado.project.grub.users.controller.dto.*;
import com.nibado.project.grub.users.repository.domain.User;
import com.nibado.project.grub.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.nibado.project.grub.validation.Values.notNull;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Access(AccessLevel.ANONYMOUS)
    public LoginResponse login(@RequestBody LoginRequest login) {
        String token = service.login(
                notNull(login.getEmail(), "email"),
                notNull(login.getPassword(), "password"));
        return new LoginResponse(token, ZonedDateTime.now().plusDays(7));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @Access(AccessLevel.ADMIN)
    public UserListResponse getAll() {
        return UserListResponse.of(service.getAll());
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public UserDTO get(@RequestAttribute("user") final User user) {
        return UserDTO.of(user);
    }

    @RequestMapping(value = "/{email:.+}", method = RequestMethod.GET)
    @Access(AccessLevel.ADMIN)
    public ResponseEntity<UserDTO> get(@PathVariable("email") final String email) {
        Optional<User> user = service.get(email);

        if(!user.isPresent()) {
            log.info("User {} not found", email);
        }

        return user
                .map(u -> ResponseEntity.ok(UserDTO.of(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/me/password", method = RequestMethod.PATCH)
    public ResponseEntity<?> updatePassword(@RequestAttribute("user") final User user, @RequestBody final UpdatePasswordDTO dto) {
        service.updatePassword(user.getEmail(), dto.getPassword());

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{email}/password", method = RequestMethod.PATCH)
    @Access(AccessLevel.ADMIN)
    public ResponseEntity<?> updatePassword(@PathVariable("email") final String email, @RequestBody final UpdatePasswordDTO dto) {
        service.updatePassword(email, dto.getPassword());

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Access(AccessLevel.ADMIN)
    public ResponseEntity<Void> create(@RequestAttribute("user") final User user, @RequestBody final CreateUserDTO createUser) {
        log.info(
                "User {} creating new user {} {} (admin={})",
                user.getEmail(),
                createUser.getName(),
                createUser.getEmail(),
                createUser.isAdmin());

        service.createUser(createUser.getEmail(), createUser.getName(), createUser.getPassword(), createUser.isAdmin());

        return ResponseEntity.noContent().build();
    }
}
