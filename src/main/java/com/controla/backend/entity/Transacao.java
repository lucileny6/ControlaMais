package com.controla.backend.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private String observacao;
    private String categoria;

    protected Transacao() {

    }

    public Long getId() {
        return id;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public  void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }
    public LocalDate getData() {
        return data;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public String getObservacao() {
        return observacao;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
