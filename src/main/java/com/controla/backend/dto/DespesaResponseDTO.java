package com.controla.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public class DespesaResponseDTO{
    public Long id;
    public BigDecimal valor;
    public LocalDate data;
    public String categoria;
    public  String descricao;
    public String observacao;


    public DespesaResponseDTO(Long id, BigDecimal valor, LocalDate data, String categoria, String descricao, String observacao){
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.observacao = observacao;

    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
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


}