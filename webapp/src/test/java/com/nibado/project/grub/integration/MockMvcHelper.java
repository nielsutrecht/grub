package com.nibado.project.grub.integration;

import com.nibado.project.grub.testsupport.Json;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

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

    public ResultActions get(final String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(headers));
    }

    public ResultActions put(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url, Json.serialize(payload))
                .headers(headers));
    }

    public ResultActions post(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url, Json.serialize(payload))
                .headers(headers));
    }

    public ResultActions patch(final String url, final Object payload) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(url, Json.serialize(payload))
                .headers(headers));
    }

    public ResultActions delete(final String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .headers(headers));
    }
}
