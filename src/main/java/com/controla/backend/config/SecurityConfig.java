package com.controla.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔹 Bean para criptografia de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔹 Configuração principal do Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desativa CSRF (necessário para chamadas externas, ex: Postman)
                .csrf(csrf -> csrf.disable())

                // Configura permissões de rota
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/register").permitAll() // rotas públicas
                        .anyRequest().authenticated() // todas as outras exigem autenticação
                )

                // 🔹 Adiciona o filtro JWT antes do filtro padrão de autenticação
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // Desativa login de formulário padrão do Spring
                .formLogin(form -> form.disable())

                // Mantém suporte a HTTP Basic (útil para testes no Postman)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
