package com.controla.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {

    private final BigDecimal saldoTotal;
    private final BigDecimal totalReceitas;
    private final BigDecimal totalDespesas;
    private final List<DashboardTransactionDTO> transacoesRecentes;
    private final List<String> acoesRapidas;

    public DashboardDTO(
            BigDecimal saldoTotal,
            BigDecimal totalReceitas,
            BigDecimal totalDespesas,
            List<DashboardTransactionDTO> transacoesRecentes,
            List<String> acoesRapidas
    ) {
        this.saldoTotal = saldoTotal;
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.transacoesRecentes = transacoesRecentes;
        this.acoesRapidas = acoesRapidas;
    }

    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }

    public BigDecimal getTotalReceitas() {
        return totalReceitas;
    }

    public BigDecimal getTotalDespesas() {
        return totalDespesas;
    }

    public List<DashboardTransactionDTO> getTransacoesRecentes() {
        return transacoesRecentes;
    }

    public List<String> getAcoesRapidas() {
        return acoesRapidas;
    }
}
