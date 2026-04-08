package com.controla.backend.dto;
import com.controla.backend.entity.TipoAcaoFinanceira;



import java.math.BigDecimal;
import java.time.LocalDate;

public class AcaoFinanceiraDTO {

    private String acao;
    private TipoAcaoFinanceira tipo;
    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private String observacao;
    private String categoria;

    public AcaoFinanceiraDTO() {

    }

    public AcaoFinanceiraDTO( TipoAcaoFinanceira tipo,

    LocalDate data,
    BigDecimal valor,
    String descricao,
    String observacao,
    String categoria){

        this.tipo = tipo;
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
        this.observacao = observacao;
        this.categoria = categoria;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public TipoAcaoFinanceira getTipo() {
        return tipo;
    }

    public void setTipo(TipoAcaoFinanceira tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
