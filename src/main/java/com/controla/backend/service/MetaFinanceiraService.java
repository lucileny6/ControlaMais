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
    public MetaFinanceiraService(MetaFinanceiraRepository metaFinanceiraRepository, UserRepository userRepository) {
        this.metaFinanceiraRepository = metaFinanceiraRepository;
        this.userRepository = userRepository;



        }
    public MetaFinanceira criarMeta(String nome, BigDecimal valorMeta) {
        if (valorMeta == null || valorMeta.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException("O valor da meta deve ser maior que zero.");
        }
        if (nome == null || nome.trim().length() == 0) {
            throw new IllegalArgumentException("O nome da meta não pode ser vazio.");
        }
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        User usuario = userRepository.findByEmail(emailUsuarioLogado);
        if (usuario == null) {
            throw new IllegalArgumentException("usário autenticado não encontrado");
        }
        List<MetaFinanceira> metasAtivas =
                metaFinanceiraRepository.findByUserEmailAndAtivaTrue(emailUsuarioLogado);

        // 4. Desativar metas antigas
        for (MetaFinanceira meta : metasAtivas) {
            meta.setAtiva(false);
        }

        metaFinanceiraRepository.saveAll(metasAtivas);

        // 5. Ativar a nova meta
        MetaFinanceira novaMeta = new MetaFinanceira();
        novaMeta.setNome(nome);
        novaMeta.setValorMeta(valorMeta);
        novaMeta.setUser(usuario);
        novaMeta.setAtiva(true);

        // 6. Salvar nova meta
        return metaFinanceiraRepository.save(novaMeta);
    }


    }

