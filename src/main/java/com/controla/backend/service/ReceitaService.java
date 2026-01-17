package com.controla.backend.service;
import java.math.BigDecimal;
import com.controla.backend.repository.ReceitaRepository;


import com.controla.backend.repository.UserRepository;
import com.controla.backend.entity.User;
import com.controla.backend.entity.Receita;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class
ReceitaService {
    private final ReceitaRepository receitaRepository;
    private final UserRepository userRepository;

    public ReceitaService(ReceitaRepository receitaRepository,
                          UserRepository userRepository) {
        this.receitaRepository = receitaRepository;
        this.userRepository = userRepository;
    }
    public Receita cadastrarReceita(Receita receita) {
        if (receita.getValor() == null || receita.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da receita deve ser maior que zero.");
        }
        if (receita.getData() == null) {
            throw new IllegalArgumentException("A data da receita é obrigatoria.");
        }
        if (receita.getCategoria() == null  || receita.getCategoria().isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatoria.");
        }
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByEmail(emailUsuarioLogado);
        if (usuario == null) {
            throw new IllegalArgumentException("usário autenticado não encontrado");
        }
        receita.setUser(usuario);
        return receitaRepository.save(receita);

    }
}
