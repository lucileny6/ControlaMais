package com.controla.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MetaFinanceiraResponseDTO {
    private Long id;
    private BigDecimal valorMeta;
    private String nome;
    private String descricao;
    private LocalDate prazo;
    private boolean ativa;

    public MetaFinanceiraResponseDTO(Long id, BigDecimal valorMeta, String nome, String descricao, LocalDate prazo, boolean ativa) {
        this.id = id;
        this.valorMeta = valorMeta;
        this.nome = nome;
        this.descricao = descricao;
        this.prazo = prazo;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public boolean isAtiva() {
        return ativa;
    }
}
