package com.nibado.project.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nibado.project.cli.dto.LoginRequestDTO;
import com.nibado.project.cli.dto.LoginResponseDTO;
import com.nibado.project.cli.dto.UpdatePasswordDTO;
import com.nibado.project.cli.dto.UserDTO;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Client {
    private static final String AUTH_HEADER = "Authorization";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final MediaType TYPE_JSON
            = MediaType.parse("application/json");

    private final OkHttpClient client = new OkHttpClient();
    private final String baseUrl;
    private String token;

    public Client(final String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    public LoginResponseDTO login(final String user, final String password) {
        Request request = request()
                .post(body(new LoginRequestDTO(user, password)))
                .url(baseUrl + "user/login")
                .build();

        LoginResponseDTO response = call(request, LoginResponseDTO.class);

        token = response.getToken();

        return response;
    }

    public UserDTO me() {
        Request request = request()
                .get()
                .url(baseUrl + "user/me")
                .build();

        return call(request, UserDTO.class);
    }

    public UserDTO user(final String email) {
        Request request = request()
                .get()
                .url(baseUrl + "user/" + encode(email))
                .build();

        return call(request, UserDTO.class);
    }

    public void updatePassword(final String password) {
        Request request = request()
                .patch(body(new UpdatePasswordDTO(password)))
                .url(baseUrl + "user/me/password")
                .build();

        call(request);
    }

    private <T> T call(final Request request, Class<T> responseClass) {
        Response response = null;

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return deserialize(response, responseClass);
            } else {
                String message = response.body().string();

                throw new RuntimeException(String.format("Call failed with code %s: %s", response.code(), message));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeResponse(response);
        }
    }

    private void call(final Request request) {
        Response response = null;

        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                String message = response.body().string();

                throw new RuntimeException(String.format("Call failed with code %s: %s", response.code(), message));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeResponse(response);
        }
    }

    private Request.Builder request() {
        Request.Builder builder = new Request.Builder();

        if(token != null) {
            builder.addHeader(AUTH_HEADER, "Bearer " + token);
        }

        return builder;
    }

    private static RequestBody body(final Object value) {
        try {
            return RequestBody.create(TYPE_JSON, MAPPER.writeValueAsBytes(value));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T deserialize(final Response response, final Class<T> clazz) {
        try {
            return MAPPER.readValue(response.body().byteStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeResponse(final Response response) {
        if (response != null && response.body() != null) {
            response.body().close();
        }
    }

    private static String encode(final String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
