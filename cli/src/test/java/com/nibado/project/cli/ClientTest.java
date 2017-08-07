package com.nibado.project.cli;

import com.nibado.project.cli.dto.UserDTO;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTest {
    private Client client;

    @Before
    public void setup() {
        client = new Client("http://localhost:8080");
    }

    @Test
    public void login() throws Exception {
        client.login("ndommerholt@gmail.com", "foo");
        UserDTO dto = client.me();

        assertThat(dto.getName()).isEqualTo("Niels");
        assertThat(dto.getEmail()).isEqualTo("ndommerholt@gmail.com");
        assertThat(dto.isAdmin()).isTrue();
    }

    @Test
    public void updatePassword() throws Exception {
        login();

        client.updatePassword("bar");

        client.login("ndommerholt@gmail.com", "bar");
    }

    @Test
    public void user() throws Exception {
        login();

        UserDTO dto = client.user("efkraneveld@hotmail.com");

        assertThat(dto.getName()).isEqualTo("Fleur");
        assertThat(dto.getEmail()).isEqualTo("efkraneveld@hotmail.com");
        assertThat(dto.isAdmin()).isTrue();
    }
}
