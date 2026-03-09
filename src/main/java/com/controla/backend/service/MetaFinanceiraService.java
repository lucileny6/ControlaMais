package com.controla.backend.service;

import com.controla.backend.dto.MetaFinanceiraResponseDTO;
import com.controla.backend.entity.MetaFinanceira;
import com.controla.backend.entity.User;
import com.controla.backend.repository.MetaFinanceiraRepository;
import com.controla.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MetaFinanceiraService {

    private final MetaFinanceiraRepository metaFinanceiraRepository;
    private final UserRepository userRepository;

    public MetaFinanceiraService(
            MetaFinanceiraRepository metaFinanceiraRepository,
            UserRepository userRepository
    ) {
        this.metaFinanceiraRepository = metaFinanceiraRepository;
        this.userRepository = userRepository;
    }

    public MetaFinanceira criarMeta(String nome, BigDecimal valorMeta, String descricao, LocalDate prazo) {

        if (valorMeta == null || valorMeta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da meta deve ser maior que zero.");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da meta nao pode ser vazio.");
        }

        String emailUsuarioLogado = getEmailUsuarioLogado();

        User usuario = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario autenticado nao encontrado")
                );

        List<MetaFinanceira> metasAtivas =
                metaFinanceiraRepository.findByUserEmailAndAtivaTrue(emailUsuarioLogado);

        for (MetaFinanceira meta : metasAtivas) {
            meta.setAtiva(false);
        }

        metaFinanceiraRepository.saveAll(metasAtivas);

        MetaFinanceira novaMeta = new MetaFinanceira();
        novaMeta.setNome(nome.trim());
        novaMeta.setDescricao(descricao != null ? descricao.trim() : null);
        novaMeta.setPrazo(prazo);
        novaMeta.setValorMeta(valorMeta);
        novaMeta.setUser(usuario);
        novaMeta.setAtiva(true);

        return metaFinanceiraRepository.save(novaMeta);
    }

    public MetaFinanceira atualizarMeta(Long id, String nome, BigDecimal valorMeta, String descricao, LocalDate prazo) {
        if (id == null) {
            throw new IllegalArgumentException("Id da meta e obrigatorio.");
        }

        if (valorMeta == null || valorMeta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da meta deve ser maior que zero.");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da meta nao pode ser vazio.");
        }

        String emailUsuarioLogado = getEmailUsuarioLogado();

        MetaFinanceira meta = metaFinanceiraRepository
                .findByIdAndUserEmailAndAtivaTrue(id, emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Meta nao encontrada para este usuario."));

        meta.setNome(nome.trim());
        meta.setDescricao(descricao != null ? descricao.trim() : null);
        meta.setPrazo(prazo);
        meta.setValorMeta(valorMeta);

        return metaFinanceiraRepository.save(meta);
    }

    public void excluirMeta(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id da meta e obrigatorio.");
        }

        String emailUsuarioLogado = getEmailUsuarioLogado();

        MetaFinanceira meta = metaFinanceiraRepository
                .findByIdAndUserEmailAndAtivaTrue(id, emailUsuarioLogado)
                .orElseThrow(() -> new IllegalArgumentException("Meta nao encontrada para este usuario."));

        metaFinanceiraRepository.delete(meta);
    }

    public List<MetaFinanceiraResponseDTO> listarMetasUsuario() {

        String emailUsuarioLogado = getEmailUsuarioLogado();

        List<MetaFinanceira> metas =
                metaFinanceiraRepository.findByUserEmailAndAtivaTrue(emailUsuarioLogado);

        return metas.stream()
                .map(meta -> new MetaFinanceiraResponseDTO(
                        meta.getId(),
                        meta.getValorMeta(),
                        meta.getNome(),
                        meta.getDescricao(),
                        meta.getPrazo(),
                        meta.isAtiva()
                ))
                .toList();
    }

    private String getEmailUsuarioLogado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
