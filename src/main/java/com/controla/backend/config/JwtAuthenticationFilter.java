package com.controla.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1️⃣ Lê o header Authorization
        String authHeader = request.getHeader("Authorization");

        // Se não tiver token, segue a requisição
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Extrai o token
        String token = authHeader.substring(7);

        try {
            // 3️⃣ Cria a chave
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            // 4️⃣ Valida e lê o token
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // 5️⃣ Pega o email do usuário (subject)
            String email = claims.getSubject();

            // 6️⃣ Verifica se pode autenticar
            if (email != null &&
                    (SecurityContextHolder.getContext().getAuthentication() == null ||
                            SecurityContextHolder.getContext().getAuthentication()
                                    instanceof AnonymousAuthenticationToken)) {

                // 7️⃣ Busca o usuário no sistema
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                // 8️⃣ Cria autenticação válida
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 9️⃣ Coloca no contexto de segurança
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {
            System.out.println("⚠️ Token inválido: " + e.getMessage());
        }

        // 🔟 Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
