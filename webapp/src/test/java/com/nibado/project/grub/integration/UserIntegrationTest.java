package com.nibado.project.grub.integration;

import com.nibado.project.grub.GrubApplication;
import com.nibado.project.grub.users.controller.dto.CreateUserDTO;
import com.nibado.project.grub.users.controller.dto.UpdatePasswordDTO;
import com.nibado.project.grub.users.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = {GrubApplication.class})
@EnableConfigurationProperties
public class UserIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserService service;

    @Autowired
    private JdbcTemplate template;

    private MockMvcHelper helper;

    @Before
    public void setup() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        helper = new MockMvcHelper(mockMvc);

        template.update("DELETE FROM users");

        service.createUser("admin@example.com", "Admin", "foo", true);
        service.createUser("user@example.com", "User", "bar", false);
    }

    @Test
    public void login() throws Exception {
        helper.login("admin@example.com", "foo");
    }

    @Test
    public void me_Admin() throws Exception {
        helper.login("admin@example.com", "foo");
        helper.get("/user/me")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Admin")))
                .andExpect(jsonPath("$.email", is("admin@example.com")))
                .andExpect(jsonPath("$.admin", is(true)));
    }

    @Test
    public void me_User() throws Exception {
        helper.login("user@example.com", "bar");
        helper.get("/user/me")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("User")))
                .andExpect(jsonPath("$.email", is("user@example.com")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    public void me_Unauthorized() throws Exception {
        helper.get("/user/me")
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is("NO_ACCESS")));
    }

    @Test
    public void get_Admin() throws Exception {
        helper.login("admin@example.com", "foo");
        helper.get("/user/user@example.com")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("User")))
                .andExpect(jsonPath("$.email", is("user@example.com")))
                .andExpect(jsonPath("$.admin", is(false)));
    }

    @Test
    public void get_User() throws Exception {
        noAccessForUser("/user/user@example.com");
    }

    @Test
    public void getAll_Admin() throws Exception {
        helper.login("admin@example.com", "foo");
        helper.get("/user")
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.users[1].name", is("User")))
                .andExpect(jsonPath("$.users[1].email", is("user@example.com")))
                .andExpect(jsonPath("$.users[1].admin", is(false)));
    }

    @Test
    public void getAll_User() throws Exception {
        noAccessForUser("/user");
    }

    @Test
    public void updatePasswordMe() throws Exception {
        helper.login("admin@example.com", "foo");

        helper.post("/user", create("updatePassword", "foo", true))
                .andExpect(status().isNoContent());

        helper.login("updatePassword@example.com", "foo");

        helper.patch("/user/me/password", password("newtest"))
                .andDo(print())
                .andExpect(status().isNoContent());

        helper.login("updatePassword@example.com", "newtest");
    }

    @Test
    public void createUserChangePassword() throws Exception {
        helper.login("admin@example.com", "foo");

        helper.post("/user", create("test", "test", true))
                .andDo(print())
                .andExpect(status().isNoContent());

        helper.patch("/user/test@example.com/password", password("newtest"))
                .andDo(print())
                .andExpect(status().isNoContent());

        helper.login("test@example.com", "newtest");
    }

    @Test
    public void createUser_NoAccess() throws Exception {
        helper.login("user@example.com", "bar");

        helper.post("/user", create("bla", "foo", true))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is("NO_ACCESS")));
    }

    @Test
    public void createUser_NullPassword() throws Exception {
        helper.login("admin@example.com", "foo");

        helper.post("/user", create("admin", null, true))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("ILLEGAL_ARGUMENT")));
    }

    @Test
    public void createUser_Duplicate() throws Exception {
        helper.login("admin@example.com", "foo");

        helper.post("/user", create("admin", "foo", true))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("ALREADY_EXISTS")));
    }

    private void noAccessForUser(String url) throws Exception {
        helper.login("user@example.com", "bar");
        helper.get(url)
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is("NO_ACCESS")));
    }

    public static CreateUserDTO create(String name, String password, boolean admin) {
        CreateUserDTO createUser = new CreateUserDTO();
        createUser.setAdmin(true);
        createUser.setPassword(password);
        createUser.setName(name);
        createUser.setEmail(name + "@example.com");

        return createUser;
    }

    public static UpdatePasswordDTO password(String password) {
        UpdatePasswordDTO passwordDTO = new UpdatePasswordDTO();

        passwordDTO.setPassword(password);

        return passwordDTO;
    }
}
