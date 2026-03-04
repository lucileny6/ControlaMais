package com.controla.backend.service;

import java.math.BigDecimal;

import com.controla.backend.dto.ReceitaRequestDTO;
import com.controla.backend.entity.Receita;
import com.controla.backend.entity.User;
import com.controla.backend.repository.ReceitaRepository;
import com.controla.backend.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final UserRepository userRepository;

    public ReceitaService(
            ReceitaRepository receitaRepository,
            UserRepository userRepository
    ) {
        this.receitaRepository = receitaRepository;
        this.userRepository = userRepository;
    }


    public Receita cadastrarReceita(Receita receita) {

        validarReceita(
                receita.getValor(),
                receita.getData(),
                receita.getCategoria()
        );

        User usuarioLogado = buscarUsuarioLogado();

        receita.setUser(usuarioLogado);

        return receitaRepository.save(receita);
    }

    public Receita atualizarReceita(Long id, ReceitaRequestDTO dto) {

        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada."));

        User usuarioLogado = buscarUsuarioLogado();

        if (!receita.getUser().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Você não tem permissão para editar esta receita.");
        }

        validarReceita(
                dto.getValor(),
                dto.getData(),
                dto.getCategoria()
        );

        receita.setValor(dto.getValor());
        receita.setData(dto.getData());
        receita.setCategoria(dto.getCategoria());
        receita.setDescricao(dto.getDescricao());
        receita.setObservacao(dto.getObservacao());

        return receitaRepository.save(receita);
    }


    public void deletarReceita(Long id) {

        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada."));

        User usuarioLogado = buscarUsuarioLogado();

        if (!receita.getUser().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Você não tem permissão para excluir esta receita.");
        }

        receitaRepository.delete(receita);
    }


    private void validarReceita(BigDecimal valor, java.time.LocalDate data, String categoria) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da receita deve ser maior que zero.");
        }

        if (data == null) {
            throw new IllegalArgumentException("A data da receita é obrigatória.");
        }

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }
    }

    private User buscarUsuarioLogado() {

        String emailUsuarioLogado =
                SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário autenticado não encontrado.")
                );
    }
}