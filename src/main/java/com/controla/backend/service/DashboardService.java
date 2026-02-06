package com.controla.backend.service;

import com.controla.backend.dto.DashboardDTO;
import com.controla.backend.dto.DashboardTransactionDTO;
import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.Receita;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.ReceitaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public DashboardService(
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository
    ) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
    }

    public DashboardDTO getDashboard() {

        // 1Usuário logado
        String emailUsuario =
                SecurityContextHolder.getContext().getAuthentication().getName();

        //  Totais
        BigDecimal totalReceitas =
                receitaRepository.somarReceitasPorUsuario(emailUsuario);
        BigDecimal totalDespesas =
                despesaRepository.somarDespesasPorUsuario(emailUsuario);

        if (totalReceitas == null) totalReceitas = BigDecimal.ZERO;
        if (totalDespesas == null) totalDespesas = BigDecimal.ZERO;

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        // Buscar receitas e despesas recentes
        List<Receita> receitasRecentes =
                receitaRepository.findTop3ByUserEmailOrderByDataDesc(emailUsuario);

        List<Despesa> despesasRecentes =
                despesaRepository.findTop3ByUserEmailOrderByDataDesc(emailUsuario);

        //  Montar transações recentes
        List<DashboardTransactionDTO> transacoesRecentes = new ArrayList<>();

        for (Receita r : receitasRecentes) {
            transacoesRecentes.add(
                    new DashboardTransactionDTO(
                            r.getId(),
                            r.getDescricao(),
                            r.getCategoria(),
                            "income",
                            r.getValor(),
                            r.getData()
                    )
            );
        }

        for (Despesa d : despesasRecentes) {
            transacoesRecentes.add(
                    new DashboardTransactionDTO(
                            d.getId(),
                            d.getDescricao(),
                            d.getCategoria(),
                            "expense",
                            d.getValor(),
                            d.getData()
                    )
            );
        }

        //  Ordenar por data (mais recente primeiro)
        transacoesRecentes.sort(
                (a, b) -> b.getData().compareTo(a.getData())
        );

        //  Limitar a 5 registros
        if (transacoesRecentes.size() > 5) {
            transacoesRecentes = transacoesRecentes.subList(0, 5);
        }

        //  Retornar DTO final
        return new DashboardDTO(
                saldo,
                totalReceitas,
                totalDespesas,
                transacoesRecentes,
                Collections.emptyList()
        );
    }
}
