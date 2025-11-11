package com.controla.backend;

import com.controla.backend.entity.User;
import com.controla.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            // criar usuário de teste
           // User user = new User();
           // user.setEmail("teste@teste.com");
           // user.setUsername("teste");
           // user.setPassword("1234");
           // user.setEnabled(true);

            // salvar usando o UserService para criptografar a senha
            //userService.registrarUser(user);
           //System.out.println("Usuário de teste criado: " + user.getEmail());
        };
    }
}
