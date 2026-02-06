package com.controla.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardTransactionDTO {

    private final Long id;
    private final String descricao;
    private final String categoria;
    private final String tipo; // income | expense
    private final BigDecimal valor;
    private final LocalDate data;

    public DashboardTransactionDTO(
            Long id,
            String descricao,
            String categoria,
            String tipo,
            BigDecimal valor,
            LocalDate data
    ) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public String getTipo() { return tipo; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getData() { return data; }
}
