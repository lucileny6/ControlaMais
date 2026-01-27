package com.controla.backend.dto;

public class ChatIAResponseDTO {

    private ChatIAResponseType tipo;
    private String mensagem;



    public ChatIAResponseDTO(){


    }

    public ChatIAResponseType getTipo() {
        return tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setTipo(ChatIAResponseType tipo) {
        this.tipo = tipo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    public enum  ChatIAResponseType {
        TEXTO,
        CONFIRMACAO;
    }
}
