package com.controla.backend.dto;


import com.controla.backend.entity.Receita;

import java.math.BigDecimal;
import java.time.LocalDate;



public class ReceitaResponseDTO {

    private Long id;
    private BigDecimal valor;
    private LocalDate data;
    private String categoria;
    private String descricao;
    private String observacao;
    private Boolean recorrente;


    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public ReceitaResponseDTO (Long id, BigDecimal valor, LocalDate data, String categoria, String descricao, String observacao, Boolean recorrente ) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.observacao = observacao;
        this.recorrente = recorrente;





    }

    public LocalDate getData() {
        return data;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacao() {
        return observacao;
    }
    public Boolean getRecorrente() {
        return recorrente;
    }
}
