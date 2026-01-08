package com.controla.backend.dto;


import java.math.BigDecimal;
import java.time.LocalDate;



public class ReceitaResponseDTO {

    private Long id;
    private BigDecimal valor;
    private LocalDate data;
    private String categoria;
    private String descricao;
    private String observacao;


    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public ReceitaResponseDTO (Long id, BigDecimal valor, LocalDate data, String categoria, String descricao, String observacao){
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.observacao = observacao;




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
