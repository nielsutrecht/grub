package com.nibado.project.grub.users.components;

import com.nibado.project.grub.users.repository.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class Jwts {
    private static final User UNAUTHORIZED_USER = new User(null, null, null);

    //TODO: Create at startup
    private String key = "supersecret";

    public User getUser(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        final String token = header.substring(7); // The part after "Bearer "
        final Claims claims;
        try {
            claims = io.jsonwebtoken.Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody();

        } catch (final SignatureException e) {
            log.warn("Invalid auth header", e);
            return UNAUTHORIZED_USER;
        }

        log.debug("User {}: {}", claims.getSubject(), claims.get("name"));

        return new User(claims.getSubject(), (String) claims.get("name"), null);
    }

    public String createToken(User user) {
        return io.jsonwebtoken.Jwts.builder().setSubject(user.getEmail())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }
}
