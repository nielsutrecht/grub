package com.nibado.project.grub.integration;

import com.nibado.project.grub.testsupport.Json;
import com.nibado.project.grub.users.controller.dto.LoginRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static com.nibado.project.grub.testsupport.Json.serialize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcHelper {
    private final MockMvc mockMvc;
    private final HttpHeaders headers;

    public MockMvcHelper(final MockMvc mockMvc) {
        this(mockMvc, new HttpHeaders());
    }

    public MockMvcHelper(final MockMvc mockMvc, final HttpHeaders headers) {
        this.mockMvc = mockMvc;
        this.headers = headers;
    }

    public void token(final String token) {
        headers.put("Authorization", Collections.singletonList("Bearer " + token));
    }

    public void clearToken() {
        headers.remove("Authorization");
    }

    public void login(String email, String password) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        MvcResult result = post("/user/login", request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.expires").exists())
                .andReturn();

        LoginResponse response = Json.deserialize(result.getResponse().getContentAsString(), LoginResponse.class);

        token(response.token);
    }

    public ResultActions get(final String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(headers));
    }

    public ResultActions put(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize((payload)))
                .headers(headers));
    }

    public ResultActions post(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize((payload)))
                .headers(headers));
    }

    public ResultActions patch(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize((payload)))
                .headers(headers));
    }

    public ResultActions delete(final String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(headers));
    }

    private static class LoginResponse {
        public String token;
        public String expires;
    }
}
