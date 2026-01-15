package com.controla.backend.dto;


import java.math.BigDecimal;

public class MetaFinanceiraResumoDTO {
    private String nome;
    private BigDecimal valorMeta;
    private BigDecimal valorAtual;
    private BigDecimal valorFaltante;
    private BigDecimal percentualConcluido;

    public String getNome() {
        return nome;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public BigDecimal getValorFaltante() {
        return valorFaltante;
    }

    public BigDecimal getPercentualConcluido() {
        return percentualConcluido;
    }

    public MetaFinanceiraResumoDTO(String nome, BigDecimal valorMeta, BigDecimal valorAtual, BigDecimal valorFaltante, BigDecimal percentualConcluido) {
        this.nome = nome;
        this.valorMeta = valorMeta;
        this.valorAtual = valorAtual;
        this.valorFaltante = valorFaltante;
        this.percentualConcluido = percentualConcluido;


    }
}
