package com.controla.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class MetaFinanceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nome;

    @Column(nullable = false)
    private BigDecimal valorMeta;

    @Column(nullable = false)
    private boolean ativa = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public MetaFinanceira() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(BigDecimal valor) {
        this.valorMeta = valor;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
