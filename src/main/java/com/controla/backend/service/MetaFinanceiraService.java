package com.controla.backend.service;

import com.controla.backend.entity.MetaFinanceira;
import com.controla.backend.entity.User;
import com.controla.backend.repository.MetaFinanceiraRepository;
import com.controla.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public MetaFinanceira criarMeta(String nome, BigDecimal valorMeta) {

        if (valorMeta == null || valorMeta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da meta deve ser maior que zero.");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da meta não pode ser vazio.");
        }

        String emailUsuarioLogado =
                SecurityContextHolder.getContext().getAuthentication().getName();

        User usuario = userRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário autenticado não encontrado")
                );

        List<MetaFinanceira> metasAtivas =
                metaFinanceiraRepository.findByUserEmailAndAtivaTrue(emailUsuarioLogado);

        // Desativar metas antigas
        for (MetaFinanceira meta : metasAtivas) {
            meta.setAtiva(false);
        }

        metaFinanceiraRepository.saveAll(metasAtivas);

        // Criar nova meta
        MetaFinanceira novaMeta = new MetaFinanceira();
        novaMeta.setNome(nome);
        novaMeta.setValorMeta(valorMeta);
        novaMeta.setUser(usuario);
        novaMeta.setAtiva(true);

        return metaFinanceiraRepository.save(novaMeta);
    }
}
