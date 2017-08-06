package com.nibado.project.grub.users.components;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashTest {
    private static final String PASSWORD = "thisissupersecret";
    @Test
    public void createValidate() throws Exception {
        String hash = Hash.create(PASSWORD);

        assertThat(Hash.validate(PASSWORD, hash)).isTrue();
        assertThat(Hash.validate("somethingelse", hash)).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_Null() {
        Hash.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_PasswordNull() {
        Hash.validate(null, Hash.create(PASSWORD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_HashNull() {
        Hash.validate(PASSWORD, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_HashInvalidLength() {
        Hash.validate(PASSWORD, "A:B");
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_HashInvalid() {
        Hash.validate(PASSWORD, "A:B:C");
    }
}
