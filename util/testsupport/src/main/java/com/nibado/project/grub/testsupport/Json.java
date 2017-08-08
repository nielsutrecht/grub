package com.nibado.project.grub.testsupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Json {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String serialize(Object payload) {
        if (payload instanceof String) {
            return (String) payload;
        } else {
            try {
                return MAPPER.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> T deserialize(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
