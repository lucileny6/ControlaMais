package com.controla.backend.service;

import com.controla.backend.dto.DashboardDTO;
import com.controla.backend.dto.DashboardTransactionDTO;
import com.controla.backend.entity.AcaoFinanceira;
import com.controla.backend.entity.Despesa;
import com.controla.backend.entity.Receita;
import com.controla.backend.entity.TipoAcaoFinanceira;
import com.controla.backend.repository.AcaoFinanceiraRepository;
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
    private final AcaoFinanceiraRepository acaoFinanceiraRepository;

    public DashboardService(
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository,
            AcaoFinanceiraRepository acaoFinanceiraRepository
    ) {
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
        this.acaoFinanceiraRepository = acaoFinanceiraRepository;
    }


    public DashboardDTO getDashboard() {

        String emailUsuario = getUsuarioLogado();

        /* Somatorio */

        BigDecimal totalReceitas = receitaRepository
                .somarReceitasPorUsuario(emailUsuario);

        BigDecimal totalDespesas = despesaRepository
                .somarDespesasPorUsuario(emailUsuario);

        if (totalReceitas == null) totalReceitas = BigDecimal.ZERO;
        if (totalDespesas == null) totalDespesas = BigDecimal.ZERO;

        BigDecimal receitasChat = acaoFinanceiraRepository
                .somarPorTipo(emailUsuario, TipoAcaoFinanceira.RECEITA);

        BigDecimal despesasChat = acaoFinanceiraRepository
                .somarPorTipo(emailUsuario, TipoAcaoFinanceira.DESPESA);

        if (receitasChat == null) receitasChat = BigDecimal.ZERO;
        if (despesasChat == null) despesasChat = BigDecimal.ZERO;

        totalReceitas = totalReceitas.add(receitasChat);
        totalDespesas = totalDespesas.add(despesasChat);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        /*  Transaçoes Rescentes */

        List<DashboardTransactionDTO> transacoesRecentes =
                listarTodasTransacoes(emailUsuario);

        if (transacoesRecentes.size() > 5) {
            transacoesRecentes = transacoesRecentes.subList(0, 5);
        }

        return new DashboardDTO(
                saldo,
                totalReceitas,
                totalDespesas,
                transacoesRecentes,
                Collections.emptyList()
        );
    }


    /*
        Listagem
    */
    public List<DashboardTransactionDTO> listarTodasTransacoes(String emailUsuario) {

        List<Receita> receitas =
                receitaRepository.findByUserEmailOrderByDataDesc(emailUsuario);

        List<Despesa> despesas =
                despesaRepository.findByUserEmailOrderByDataDesc(emailUsuario);

        List<AcaoFinanceira> acoes =
                acaoFinanceiraRepository.findByUsuarioEmailOrderByDataDesc(emailUsuario);

        List<DashboardTransactionDTO> lista = new ArrayList<>();

        /* Receitas */
        for (Receita r : receitas) {
            lista.add(new DashboardTransactionDTO(
                    r.getId(),
                    r.getDescricao(),
                    r.getCategoria(),
                    "income",
                    r.getValor(),
                    r.getData(),
                    r.getRecorrente()
            ));
        }

        /* Despesas */
        for (Despesa d : despesas) {
            lista.add(new DashboardTransactionDTO(
                    d.getId(),
                    d.getDescricao(),
                    d.getCategoria(),
                    "expense",
                    d.getValor(),
                    d.getData(),
                    d.getRecorrente()
            ));
        }

        /* Chat IA */
        for (AcaoFinanceira a : acoes) {

            String tipo = a.getTipo() == TipoAcaoFinanceira.RECEITA
                    ? "income"
                    : "expense";

            lista.add(new DashboardTransactionDTO(
                    a.getId(),
                    a.getDescricao(),
                    a.getCategoria(),
                    tipo,
                    a.getValor(),
                    a.getData(),
                    false
            ));
        }

        /* ===== ORDENAÇÃO FINAL ===== */
        lista.sort((a, b) -> b.getData().compareTo(a.getData()));

        return lista;
    }


    private String getUsuarioLogado() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }


}

