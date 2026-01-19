package com.controla.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY =
            "minhaChaveSuperSecreta1234567890minhaChaveSuperSecreta";

    private final UserDetailsService userDetailsService;

    // 🔹 Injeção do serviço que sabe buscar usuários
    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 🔹 1. Lê o header Authorization
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 2. Extrai o token
        String token = header.substring(7);

        try {
            // 🔹 3. Cria a chave
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            // 🔹 4. Valida e lê o token
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 🔹 5. Subject = email
            String email = claims.getSubject();

            // 🔹 6. Se ainda não estiver autenticado
            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 🔹 7. Busca o usuário REAL no sistema
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                // 🔹 8. Cria autenticação válida para o Spring
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 🔹 9. Coloca no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            System.out.println("⚠️ Token inválido: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
