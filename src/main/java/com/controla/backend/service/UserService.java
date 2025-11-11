package com.controla.backend.service;

import com.controla.backend.entity.User;
import com.controla.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Cadastro de usuário
    public User registrarUser(User user) {
        // Verifica se o usuário já existe
        Optional<User> existente = findByEmail(user.getEmail());
        if (existente.isPresent()) {
            System.out.println("⚠️ Usuário já existe: " + user.getEmail());
            return existente.get();
        }

        // Criptografa a senha e habilita o usuário
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Salva no banco
        User savedUser = userRepository.save(user);
        System.out.println("✅ Usuário cadastrado: " + savedUser.getEmail());
        return savedUser;
    }

    // 🔹 Validação de login
    public boolean validateLogin(String email, String rawPassword) {
        Optional<User> userOpt = findByEmail(email);

        if (userOpt.isEmpty()) {
            System.out.println("❌ Usuário não encontrado: " + email);
            return false;
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            System.out.println("❌ Senha incorreta para o e-mail: " + email);
            return false;
        }

        if (!user.isEnabled()) {
            System.out.println("⚠️ Usuário desativado: " + email);
            return false;
        }

        System.out.println("✅ Login validado com sucesso: " + email);
        return true;
    }

    // 🔹 Busca por e-mail
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
