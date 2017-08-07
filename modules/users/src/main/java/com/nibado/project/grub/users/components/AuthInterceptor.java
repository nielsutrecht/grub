package com.nibado.project.grub.users.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public static final String AUTH_HEADER = "Authorization";
    private final Jwts jwts;

    @Autowired
    public AuthInterceptor(final Jwts jwts) {
        this.jwts = jwts;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("user", jwts.getUser(request.getHeader(AUTH_HEADER)));

        return true;
    }
}
