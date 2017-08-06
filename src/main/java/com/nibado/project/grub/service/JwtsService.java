package com.nibado.project.grub.service;

import com.nibado.project.grub.repository.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class JwtsService {
    private static final User UNAUTHORIZED_USER = new User(null, null, null);

    private String key = "supersecret";

    public User getUser(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        final String token = header.substring(7); // The part after "Bearer "
        final Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody();

        } catch (final SignatureException e) {
            log.warn("Invalid auth header", e);
            return UNAUTHORIZED_USER;
        }

        log.debug("User {}: {}", claims.getSubject(), claims.get("name"));

        return new User(claims.getSubject(), (String) claims.get("name"), null);
    }

    public String createToken(User user) {
        return Jwts.builder().setSubject(user.getEmail())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }
}
