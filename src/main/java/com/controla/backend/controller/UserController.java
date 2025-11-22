package com.controla.backend.controller;

import com.controla.backend.entity.User;
import com.controla.backend.service.UserService;
import com.controla.backend.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8081")


public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    // 🔹 CADASTRO DE USUÁRIO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User novoUser = userService.registrarUser(user);

            // Retorna apenas dados não sensíveis
            Map<String, Object> response = Map.of(
                    "id", novoUser.getId(),
                    "email", novoUser.getEmail(),
                    "username", novoUser.getUsername(),
                    "message", "Cadastro realizado com sucesso!"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Erro ao cadastrar usuário: " + e.getMessage()));
        }
    }

    // 🔹 LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean valid = userService.validateLogin(user.getEmail(), user.getPassword());

        if (valid) {
            // Gera token JWT
            String token = JwtUtil.gerarToken(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "message", "Login realizado com sucesso!",
                    "success", true,
                    "token", token
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Usuário ou senha inválidos!",
                    "success", false
            ));
        }
    }
}
