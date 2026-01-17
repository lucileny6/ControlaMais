package com.controla.backend.service;

import com.controla.backend.dto.DashboardDTO;
import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.MetaFinanceira;
import com.controla.backend.entity.Receita;
import com.controla.backend.repository.DespesaRepository;
import com.controla.backend.repository.MetaFinanceiraRepository;
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


    public DashboardService(ReceitaRepository receitaRepository,
                            DespesaRepository despesaRepository) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;

    }

    public DashboardDTO getDashboard() {

        // 1. Descobrir quem é o usuário logado
        String emailUsuarioLogado =
                SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Buscar a soma das despesas no banco
        BigDecimal totalReceitas = receitaRepository.somarReceitasPorUsuario(emailUsuarioLogado);

        //3. Buscar a soma das despesas no banco
        BigDecimal totalDespesas = despesaRepository.somarDespesasPorUsuario(emailUsuarioLogado);

        // 4. Tratar null (quando não existe nenhuma receita ou despesa)
        if (totalReceitas == null) {
            totalReceitas = BigDecimal.ZERO;
        }
        if (totalDespesas == null) {
            totalDespesas = BigDecimal.ZERO;
        }
        // 5. Calcular o saldo
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        //6.Resceitas recentes
        List<Receita> receitasRecentes =
                receitaRepository.findTop3ByUserEmailOrderByDataDesc(emailUsuarioLogado);
        List<String> transacoesRecentes = new ArrayList<>();
        for (Receita r : receitasRecentes) {
            transacoesRecentes.add(
                    "+ " + r.getDescricao() + " R$ " + r.getValor()
            );
        }
            List<Despesa> despesasRecentes = despesaRepository.findTop3ByUserEmailOrderByDataDesc(emailUsuarioLogado);
            for (Despesa despesa : despesasRecentes) {
                transacoesRecentes.add(
                        "- " + despesa.getDescricao() + " R$ " + despesa.getValor()
                );
            }



            // 7. Montar o DTO do Dashboard
            return new DashboardDTO(
                    saldo,
                    totalReceitas,
                    totalDespesas,
                    transacoesRecentes,  // transações recentes (depois)
                    Collections.emptyList()   // ações rápidas (depois)
            );
        }
   }






