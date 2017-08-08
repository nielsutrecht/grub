package com.nibado.project.grub.integration;

import com.nibado.project.grub.GrubApplication;
import com.nibado.project.grub.users.controller.dto.LoginRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = {GrubApplication.class})
@EnableConfigurationProperties
public class UserIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvcHelper helper;

    @Before
    public void setup() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();

        helper = new MockMvcHelper(mockMvc);
    }

    @Test
    public void login() throws Exception {
        login("admin@example.com", "foo");
    }

    public void login(String email, String password) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        helper.post("/user/login", new LoginRequest())
                .andDo(print());
    }
}
