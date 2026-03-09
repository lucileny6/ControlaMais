package com.controla.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "minhaChaveSuperSecreta1234567890minhaChaveSuperSecreta";

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static String gerarToken(String email) {
        SecretKey key = getSigningKey();

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(key)
                .compact();
    }

    public static String extrairEmailDoToken(String token) {
        SecretKey key = getSigningKey();

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public static boolean tokenValido(String token) {
        try {
            extrairEmailDoToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
