package com.nibado.project.cli;

import com.nibado.project.cli.dto.LoginResponseDTO;
import com.nibado.project.cli.dto.UserDTO;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Reader implements Runnable {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Client client;

    private LoginResponseDTO loginResponse;

    public Reader(final Client client) {
        this.client = client;
    }

    public Reader(final Client client, final String user, final String password) {
        this(client);
        login(user, password);
    }

    public void execute(final String... commands) {

    }

    public void login(final String user, final String password) {
        loginResponse = client.login(user, password);
        UserDTO me = client.me();

        ZonedDateTime expires = ZonedDateTime.parse(loginResponse.getExpires(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Duration duration = Duration.between(ZonedDateTime.now(), expires);

        System.out.printf("Logged in as user %s, admin: %s, expires: %s (%s)",
                me.getName(),
                me.isAdmin(),
                expires.format(DATE_TIME_FORMATTER),
                format(duration));
    }

    private static String format(final Duration duration) {
        if (duration.toHours() >= 24) {
            return duration.toDays() + " days";
        } else if (duration.toMinutes() >= 60) {
            return duration.toHours() + " hours";
        } else {
            return duration.toMinutes() + " minutes";
        }
    }

    @Override
    public void run() {

    }
}
