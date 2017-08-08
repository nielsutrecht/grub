package com.nibado.project.grub.integration;

import com.nibado.project.grub.GrubApplication;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = {GrubApplication.class})
@EnableConfigurationProperties
public class IngredientIntegrationTest {
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

        helper.login("admin@example.com", "foo");
    }

    @Test
    public void getIngredients() throws Exception {
        helper.get("/ingredient")
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getIngredients_NotLoggedIn() throws Exception {
        helper.clearToken();

        helper.get("/ingredient")
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is("NO_ACCESS")));
    }
}
