package com.controla.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    private final BigDecimal saldoTotal;
    private final BigDecimal totalReceitas;
    private final BigDecimal totalDespesas;
    private final BigDecimal meta;
    private final BigDecimal poupanca;
    private final  List<String> transacoesRecentes;
    private final List<String> acoesRapidas;

    public DashboardDTO(BigDecimal saldoTotal, BigDecimal totalReceitas, BigDecimal totalDespesas,
                        BigDecimal meta, BigDecimal poupanca,
                        List<String> transacoesRecentes, List<String> acoesRapidas) {
        this.saldoTotal = saldoTotal;
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.meta = meta;
        this.poupanca = poupanca;
        this.transacoesRecentes = transacoesRecentes;
        this.acoesRapidas = acoesRapidas;
    }

    // getters e setters
    public BigDecimal getSaldoTotal() { return saldoTotal; }
    public BigDecimal getTotalReceitas() { return totalReceitas; }
    public BigDecimal getTotalDespesas() { return totalDespesas; }
    public BigDecimal getMeta() { return meta; }
    public BigDecimal getPoupanca() { return poupanca; }
    public List<String> getTransacoesRecentes() { return transacoesRecentes; }
    public List<String> getAcoesRapidas() { return acoesRapidas; }
}
