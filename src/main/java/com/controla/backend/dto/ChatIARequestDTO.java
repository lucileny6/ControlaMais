package com.controla.backend.dto;

public class ChatIARequestDTO {
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


    public ChatIARequestDTO(){

    }
    public ChatIARequestDTO(String mensagem) {
        this.mensagem = mensagem;

    }
}

