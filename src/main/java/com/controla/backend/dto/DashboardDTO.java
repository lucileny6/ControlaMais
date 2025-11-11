package com.controla.backend.dto;

import java.util.List;

public class DashboardDTO {
    private double saldoTotal;
    private double totalReceitas;
    private double totalDespesas;
    private double meta;
    private double poupanca;
    private List<String> transacoesRecentes;
    private List<String> acoesRapidas;

    public DashboardDTO(double saldoTotal, double totalReceitas, double totalDespesas,
                        double meta, double poupanca,
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
    public double getSaldoTotal() { return saldoTotal; }
    public double getTotalReceitas() { return totalReceitas; }
    public double getTotalDespesas() { return totalDespesas; }
    public double getMeta() { return meta; }
    public double getPoupanca() { return poupanca; }
    public List<String> getTransacoesRecentes() { return transacoesRecentes; }
    public List<String> getAcoesRapidas() { return acoesRapidas; }
}
