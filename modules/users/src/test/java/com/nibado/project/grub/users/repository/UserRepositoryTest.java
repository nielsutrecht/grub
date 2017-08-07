package com.nibado.project.grub.users.repository;

import com.nibado.project.grub.repository.RepositoryTest;
import com.nibado.project.grub.users.repository.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest extends RepositoryTest<User> {
    private UserRepository repository;

    public UserRepositoryTest() {
        super("users");
    }

    @Before
    public void setup() {
        super.setup();
        repository = new UserRepository(template);
    }

    @Test
    public void findByEmail() throws Exception {
        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test2@example.com", "Test 2", "Password 2", false);

        User u = repository.findByEmail("test1@example.com").orElseThrow(() -> new RuntimeException("Fail"));

        assertThat(u.getId()).isNotNull();
        assertThat(u.getName()).isEqualTo("Test 1");
        assertThat(u.getEmail()).isEqualTo("test1@example.com");
        assertThat(u.getPassword()).isEqualTo("Password 1");
        assertThat(u.isAdmin()).isTrue();

        assertThat(repository.findByEmail("foo@example.com")).isEmpty();
    }

    @Test
    public void createUser() throws Exception {
        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test2@example.com", "Test 2", "Password 2", false);

        assertRows(2);
    }

    @Test
    public void createUser_UniqueEmail() throws Exception {
        expected.expect(DuplicateKeyException.class);

        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test1@example.com", "Test 2", "Password 2", false);
    }

    @Test
    public void findAll() throws Exception {
        assertThat(repository.findAll()).isEmpty();

        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test2@example.com", "Test 2", "Password 2", false);

        assertRows(2);

        List<User> users = repository.findAll();

        assertThat(users).hasSize(2);

        assertThat(users).extracting("id").hasSize(2).isNotNull();
        assertThat(users).extracting("email").contains("test1@example.com", "test2@example.com");
        assertThat(users).extracting("name").contains("Test 1", "Test 2");
        assertThat(users).extracting("password").contains("Password 1", "Password 2");
        assertThat(users).extracting("admin").contains(true, false);
    }

    @Test
    public void delete() throws Exception {
        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test2@example.com", "Test 2", "Password 2", false);

        assertRows(2);

        repository.deleteUser("test1@example.com");

        assertRows(1);

        repository.deleteUser("test2@example.com");

        assertEmpty();
    }

    @Test
    public void count() {
        assertThat(repository.count()).isZero();

        repository.createUser("test1@example.com", "Test 1", "Password 1", true);
        repository.createUser("test2@example.com", "Test 2", "Password 2", false);

        assertThat(repository.count()).isEqualTo(2);
    }
}
