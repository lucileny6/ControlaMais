package com.controla.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "minhaChaveSuperSecreta1234567890minhaChaveSuperSecreta";

    public static String gerarToken(String email) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeKey()));

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    public static String extrairEmailDoToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeKey()));

        Claims claims = Jwts.parser()       // ✅ versão correta para jjwt 0.12.6
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

    private static String encodeKey() {
        return java.util.Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }
}
