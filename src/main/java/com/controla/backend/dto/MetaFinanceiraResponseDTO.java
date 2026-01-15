package com.controla.backend.dto;

import java.math.BigDecimal;

public class MetaFinanceiraResponseDTO {
    private Long id;
    private BigDecimal valorMeta;
    private String nome;
    private boolean ativa;

    public Long getId() {
        return id;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public String getNome() {
        return nome;
    }

    public MetaFinanceiraResponseDTO (Long id, BigDecimal valorMeta, String nome, boolean ativa) {
        this.id = id;
        this.valorMeta = valorMeta;
        this.nome = nome;
        this.ativa = ativa;


    }
}
