package com.controla.backend.service;

import java.math.BigDecimal;

import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.User;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final UserRepository userRepository;

    public DespesaService(
            DespesaRepository despesaRepository,
            UserRepository userRepository
    ) {
        this.despesaRepository = despesaRepository;
        this.userRepository = userRepository;
    }

    public Despesa cadastrarDespesa(Despesa despesa) {

        if (despesa.getValor() == null || despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da despesa deve ser maior que zero.");
        }

        if (despesa.getData() == null) {
            throw new IllegalArgumentException("A data da despesa é obrigatória.");
        }

        if (despesa.getCategoria() == null || despesa.getCategoria().isBlank()) {
            throw new IllegalArgumentException("A categoria da despesa é obrigatória.");
        }

        String emailUsuarioLogado =
                SecurityContextHolder.getContext().getAuthentication().getName();

        User usuario = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário autenticado não encontrado")
                );

        despesa.setUser(usuario);

        return despesaRepository.save(despesa);
    }
}
