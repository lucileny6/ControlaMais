package com.controla.backend.service;

import com.controla.backend.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DashboardService {

    public DashboardDTO getDashboard() {
        return new DashboardDTO(
                9999.0,   // saldoTotal
                8888.0,   // totalReceitas
                777.0,    // totalDespesas
                5000.0,   // meta
                2500.0,    // poupanca
                Arrays.asList(
                        "Salário R$8888",
                        "Supermercado R$777",
                        "Conta de luz R$500"
                ), // transações recentes
                Arrays.asList(
                        "Adicionar Receita",
                        "Adicionar Despesa",
                        "Conversar com IA",
                        "Ver Relatórios"
                ) // ações rápidas
        );
    }
}

