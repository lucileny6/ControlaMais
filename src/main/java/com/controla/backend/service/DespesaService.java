package com.controla.backend.service;

import java.math.BigDecimal;

import com.controla.backend.dto.DespesaRequestDTO;
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

        validarDespesa(
                despesa.getValor(),
                despesa.getData(),
                despesa.getCategoria()
        );

        User usuarioLogado = buscarUsuarioLogado();
        despesa.setUser(usuarioLogado);

        return despesaRepository.save(despesa);
    }

    // =========================
    // ATUALIZAR
    // =========================
    public Despesa atualizarDespesa(Long id, DespesaRequestDTO dto) {

        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Despesa nÃ£o encontrada."));

        User usuarioLogado = buscarUsuarioLogado();

        if (!despesa.getUser().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("VocÃª nÃ£o tem permissÃ£o para editar esta despesa.");
        }

        validarDespesa(
                dto.getValor(),
                dto.getData(),
                dto.getCategoria()
        );

        despesa.setValor(dto.getValor());
        despesa.setData(dto.getData());
        despesa.setCategoria(dto.getCategoria());
        despesa.setDescricao(dto.getDescricao());
        despesa.setObservacao(dto.getObservacao());
        despesa.setRecorrente(dto.getRecorrente());

        return despesaRepository.save(despesa);
    }

    // =========================
    // DELETAR
    // =========================
    public void deletarDespesa(Long id) {

        User usuarioLogado = buscarUsuarioLogado();

        despesaRepository.findById(id).ifPresent(despesa -> {
            if (!despesa.getUser().getId().equals(usuarioLogado.getId())) {
                throw new SecurityException("VocÃª nÃ£o tem permissÃ£o para excluir esta despesa.");
            }

            despesaRepository.delete(despesa);
        });
    }

    // =========================
    // MÃ‰TODOS AUXILIARES
    // =========================

    private void validarDespesa(BigDecimal valor, java.time.LocalDate data, String categoria) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da despesa deve ser maior que zero.");
        }

        if (data == null) {
            throw new IllegalArgumentException("A data da despesa Ã© obrigatÃ³ria.");
        }

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria Ã© obrigatÃ³ria.");
        }
    }

    private User buscarUsuarioLogado() {

        String emailUsuarioLogado =
                SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() ->
                        new IllegalArgumentException("UsuÃ¡rio autenticado nÃ£o encontrado.")
                );
    }
}

